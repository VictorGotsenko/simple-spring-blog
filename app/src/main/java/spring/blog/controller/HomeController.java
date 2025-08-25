package spring.blog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "Добро пожаловать в Hexlet Spring Blog!";
    }

    //у блога есть отдельная страница «О проекте». less 03
    @GetMapping("/about")
    public String about() {
        return "This is simple Spring blog!";
    }

}