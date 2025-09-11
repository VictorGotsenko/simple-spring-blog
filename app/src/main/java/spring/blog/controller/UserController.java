package spring.blog.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import spring.blog.exception.ResourceNotFoundException;
import spring.blog.model.User;
import spring.blog.repository.UserRepository;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // http get localhost:8080/api/users
    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<User> indexUsers(@RequestParam(defaultValue = "10") Integer limit) {
        return userRepository.findAll();
    }

    // http get localhost:8080/api/users/1
    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User show(@PathVariable Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id:" + id + " - Not Found"));
    }

    // http post localhost:8080/api/users id=1 name=Chuk email=chuk@mail.io
    // http post localhost:8080/api/users id=2 name=Gek email=gek@mail.io
    //    @PostMapping("/users") // Создание
    // http post localhost:8080/api/users firstName=Chuk lastName=Gai email=chuk@mail.io birthday=2000-01-10
    // http post localhost:8080/api/users firstName=Alise lastName=Fox email=alise@mail.io birthday=2001-01-10
    // http post localhost:8080/api/users firstName=Chuk lastName=Gai birthday=2000-01-01 email=chuk@mail.io
    @PostMapping("/users")
    public ResponseEntity<User> create(@RequestBody User user) {
        User saved = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // http put localhost:8080/api/users/1 name=Bob email=bob@hotmail.io
    //http put localhost:8080/api/users/2 firstName=Chuk lastName=Gai email=chuk@mail.io birthday=2000-01-01
    @PutMapping("/users/{id}") // Обновление
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@Valid @PathVariable Long id, @RequestBody User data) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id:" + id + " - Not Found"));
        user.setFirstName(data.getFirstName());
        user.setLastName(data.getLastName());
        user.setEmail(data.getEmail());
        user.setBirthday(data.getBirthday());
        userRepository.save(user);
        return user;
    }

    // http delete localhost:8080/api/users/2
    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}
