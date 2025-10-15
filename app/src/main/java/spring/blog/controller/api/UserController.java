package spring.blog.controller.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.server.ResponseStatusException;
import spring.blog.dto.UserCreateDTO;
import spring.blog.dto.UserDTO;
import spring.blog.dto.UserPatchDTO;
import spring.blog.dto.UserUpdateDTO;
import spring.blog.exception.ResourceNotFoundException;
import spring.blog.mapper.UserMapper;
import spring.blog.model.User;
import spring.blog.repository.UserRepository;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
/*
    UserController(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }
*/

    /**
     * Это функция GET.
     *
     * @param limit limit
     * @return findAll
     */
    // http get localhost:8080/api/users
    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> indexUsers(@RequestParam(defaultValue = "10") Integer limit) {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

    /**
     * Это функция GET.
     *
     * @param id id
     * @return user by {id}
     */
    // http get localhost:8080/api/users/1
    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO show(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> userMapper.toDTO(user))
                .orElseThrow(() -> new ResourceNotFoundException("User with id:" + id + " - Not Found"));
    }

    // http post localhost:8080/api/users id=1 name=Chuk email=chuk@mail.io
    // http post localhost:8080/api/users id=2 name=Gek email=gek@mail.io
    //    @PostMapping("/users") // Создание
    // http post localhost:8080/api/users firstName=Chuk lastName=Gai email=chuk@mail.io birthday=2000-01-10
    // http post localhost:8080/api/users firstName=Alise lastName=Fox email=alise@mail.io birthday=2001-01-10
    // http post localhost:8080/api/users firstName=Chuk lastName=Gai birthday=2000-01-01 email=chuk@mail.io

    /**
     * Это функция POST Create.
     *
     * @param dto user
     * @return created user
     */
    @PostMapping("/users")
    public ResponseEntity<UserDTO> create(@Valid @RequestBody UserCreateDTO dto) {
        User user = userMapper.toEntity(dto);
        userRepository.save(user);
//        return ResponseEntity.ok(userMapper.toDTO(user));
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toDTO(user));
    }

    // http put localhost:8080/api/users/1 name=Bob email=bob@hotmail.io
    //http put localhost:8080/api/users/2 firstName=Chuk lastName=Gai email=chuk@mail.io birthday=2000-01-01

    /**
     * Это функция PUT.
     *
     * @param id  id
     * @param dto data
     * @return userDTO
     */
    @PutMapping("/users/{id}") // Обновление
    public ResponseEntity<UserDTO> update(@PathVariable Long id,
                                          @Valid @RequestBody UserUpdateDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));

        userMapper.updateEntityFromDTO(dto, user);
        userRepository.save(user);

        return ResponseEntity.ok(userMapper.toDTO(user));
    }

    /**
     *
     * @param id
     * @param dto
     * @return ResponseEntity ResponseEntity
     */
    @PatchMapping("/users/{id}")
    public ResponseEntity<UserDTO> patchUser(@PathVariable Long id,
                                             @RequestBody UserPatchDTO dto) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        dto.getFirstName().ifPresent(user::setFirstName);
        dto.getLastName().ifPresent(user::setLastName);
        dto.getEmail().ifPresent(user::setEmail);

        userRepository.save(user);
        return ResponseEntity.ok(userMapper.toDTO(user));
    }

    // http delete localhost:8080/api/users/2

    /**
     * Это функция DELETE.
     *
     * @param id id
     */
    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}
