package spring.blog.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Это описание класса, который выполняет определенные действия.
 *
 * @author Имя автора
 * @version 1.0
 */
@RestController
public class WelcomeController {

    @Value("${app.welcome-message}")
    private String welcomeMessage;

    /**
     * Это функция welcome.
     * @return welcomeMessage
     * @author Имя автора
     * @version 1.0
     * localhost:8080/welcome
     */
    @GetMapping("/welcome")
    public String welcome() {
        return welcomeMessage;
    }
}
