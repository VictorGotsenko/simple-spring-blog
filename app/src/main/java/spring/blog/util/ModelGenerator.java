package spring.blog.util;

import jakarta.annotation.PostConstruct;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;
import spring.blog.model.Post;
import spring.blog.model.User;
import spring.blog.repository.PostRepository;
import spring.blog.repository.UserRepository;

/**
 * Это описание класса, который выполняет определенные действия.
 *
 * @author Имя автора
 * @version 1.0
 */
@Component
public class ModelGenerator {

    private final Faker faker;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public ModelGenerator(Faker faker, UserRepository userRepository, PostRepository postRepository) {
        this.faker = faker;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    /**
     * Это функция, которая заполняет базы.
     *
     * @author Имя автора
     * @version 1.0
     */
    @PostConstruct
    public void generateData() {
        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setFirstName(faker.name().firstName());
            user.setLastName(faker.name().lastName());
            user.setEmail(faker.internet().emailAddress());
            userRepository.save(user);

            Post post = new Post();
            post.setTitle(faker.book().title());
            post.setContent(faker.lorem().sentence());
            post.setPublished(faker.bool().bool());
            post.setUserId(user.getId());
            postRepository.save(post);
            System.out.println("Id: " + post.getId());
            System.out.println("Title: " + post.getTitle());
            System.out.println("User Id: " + post.getUserId());
            System.out.println("Content: " + post.getContent());
            System.out.println("CreatetAt: " + post.getCreatedAt());
        }
    }
}
