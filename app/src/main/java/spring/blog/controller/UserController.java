package spring.blog.controller;


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
import org.springframework.web.bind.annotation.RestController;

import spring.blog.Application;
import spring.blog.model.Post;
import spring.blog.model.User;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {


    // http get localhost:8080/api/users
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(@RequestParam(defaultValue = "10") Integer limit) {
        var result = Application.getUsers()
                .stream().limit(limit).toList();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(Application.getUsers().size()))
                .body(result);
    }

    // http get localhost:8080/api/users/1
    @GetMapping("/users/{id}")
    public ResponseEntity<User> showUser(@PathVariable Long id) {
        Optional<User> result = Application.getUsers().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
        return ResponseEntity.of(result);
    }

    // http post localhost:8080/api/users id=1 name=Chuk email=chuk@mail.io
    // http post localhost:8080/api/users id=2 name=Gek email=gek@mail.io
    @PostMapping("/users") // Создание
    public ResponseEntity<User> createUser(@RequestBody User user) {
        // валидация: например, чтобы email не были пустыми.
        if (null == user.getEmail()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(user);
        }
        List<User> users = Application.getUsers();
        users.add(user);
        Application.setUsers(users);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }


    // http put localhost:8080/api/users/1 name=Bob email=bob@hotmail.io
    @PutMapping("/users/{id}") // Обновление
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User data) {
        List<User> users = Application.getUsers();

        Optional<User> maybeUser = users.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
        if (maybeUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        User user = maybeUser.get();
        user.setId(data.getId());
        user.setName(data.getName());
        user.setEmail(data.getEmail());
        Application.setUsers(users);
        return ResponseEntity.ok().body(user);
    }

    // http delete localhost:8080/api/users/2
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        List<User> users = Application.getUsers();
        users.removeIf(p -> p.getId().equals(id));
        Application.setUsers(users);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
