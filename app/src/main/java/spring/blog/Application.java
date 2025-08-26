package spring.blog;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;
import spring.blog.model.Post;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RestController
public class Application {
    // Хранилище добавленных постов
    @Setter
    @Getter
    private static List<Post> posts= new ArrayList<>();


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
