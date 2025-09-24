package spring.blog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

      /**
     * Это функция Main pag.
     * @return Main pag
     * @author Имя автора
     * @version 1.0
     */
    @GetMapping("/")
    public String home() {
        return "Main page Simple Spring Blog!";
    }

      /**
     * Это функция страница «О проекте».
     * @return страница «О проекте»
     * @author Имя автора
     * @version 1.0
     */
    //у блога есть отдельная страница «О проекте». less 03
    @GetMapping("/about")
    public String about() {
        return "This is simple Spring blog!";
    }
}
