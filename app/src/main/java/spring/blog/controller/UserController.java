package spring.blog.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import spring.blog.model.User;
import spring.blog.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // http get localhost:8080/api/users
//    @GetMapping("/users")
//    public ResponseEntity<List<User>> getAllUsers(@RequestParam(defaultValue = "10") Integer limit) {
//        var result = Application.getUsers()
//                .stream().limit(limit).toList();
//        return ResponseEntity.ok()
//                .header("X-Total-Count", String.valueOf(Application.getUsers().size()))
//                .body(result);
//    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<User> indexUsers(@RequestParam(defaultValue = "10") Integer limit) {
        return userRepository.findAll();
    }


    // http get localhost:8080/api/users/1
//    @GetMapping("/users/{id}")
//    public ResponseEntity<User> showUser(@PathVariable Long id) {
//        Optional<User> result = Application.getUsers().stream()
//                .filter(p -> p.getId().equals(id))
//                .findFirst();
//        return ResponseEntity.of(result);
//    }


    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User show(@PathVariable Long id) {
        var user = userRepository.findById(id).get();
        return user;
    }


    // http post localhost:8080/api/users id=1 name=Chuk email=chuk@mail.io
    // http post localhost:8080/api/users id=2 name=Gek email=gek@mail.io
//    @PostMapping("/users") // Создание
//    public ResponseEntity<User> createUser(@RequestBody User user) {
//        // валидация: например, чтобы email не были пустыми.
//        if (null == user.getEmail()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(user);
//        }
//        List<User> users = Application.getUsers();
//        users.add(user);
//        Application.setUsers(users);
//        return ResponseEntity.status(HttpStatus.CREATED).body(user);
//    }


    // http post localhost:8080/api/users firstName=Chuk lastName=Gai email=chuk@mail.io birthday=2000-01-10
    // http post localhost:8080/api/users firstName=Alise lastName=Fox email=alise@mail.io birthday=2001-01-10
//    http post localhost:8080/api/users firstName=Chuk lastName=Gai birthday=2000-01-01 email=chuk@mail.io
    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) {
        userRepository.save(user);
        return user;
    }


    // http put localhost:8080/api/users/1 name=Bob email=bob@hotmail.io
//    @PutMapping("/users/{id}") // Обновление
//    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User data) {
//        List<User> users = Application.getUsers();
//
//        Optional<User> maybeUser = users.stream()
//                .filter(p -> p.getId().equals(id))
//                .findFirst();
//        if (maybeUser.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//        User user = maybeUser.get();
//        user.setId(data.getId());
//        user.setName(data.getName());
//        user.setEmail(data.getEmail());
//        Application.setUsers(users);
//        return ResponseEntity.ok().body(user);
//    }


    //http put localhost:8080/api/users/2 firstName=Chuk lastName=Gai email=chuk@mail.io birthday=2000-01-01
    @PutMapping("/users/{id}") // Обновление
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User data) {
//        var user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new); // Поиск сущности по Id;
        Optional<User> maybeUser = userRepository.findById(id); // Поиск сущности по Id;
        if (maybeUser.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            return ResponseEntity.of(maybeUser);
        }
        User user = maybeUser.get();
        user.setFirstName(data.getFirstName());
        user.setLastName(data.getLastName());
        user.setEmail(data.getEmail());
        user.setBirthday(data.getBirthday());
        userRepository.save(user);
        return ResponseEntity.ok().body(user);
    }

    // http delete localhost:8080/api/users/2
//    @DeleteMapping("/users/{id}")
//    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
//        List<User> users = Application.getUsers();
//        users.removeIf(p -> p.getId().equals(id));
//        Application.setUsers(users);
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}
