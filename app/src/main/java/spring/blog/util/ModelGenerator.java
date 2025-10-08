package spring.blog.util;

import jakarta.annotation.PostConstruct;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;
import spring.blog.model.Post;
import spring.blog.model.Tag;
import spring.blog.model.User;
import spring.blog.repository.PostRepository;
import spring.blog.repository.TagRepository;
import spring.blog.repository.UserRepository;

import java.util.List;

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
    private final TagRepository tagRepository;

    public ModelGenerator(Faker faker,
                          UserRepository userRepository,
                          PostRepository postRepository,
                          TagRepository tagRepository) {
        this.faker = faker;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
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
            post.setAuthor(user);
            postRepository.save(post);

            user.setPosts(List.of(post));
            userRepository.save(user);


            Tag tag1 = new Tag();
            tag1.setName(faker.gameOfThrones().house());
            tag1.setPost(post);
            Tag tag2 = new Tag();
            tag2.setName(faker.gameOfThrones().house());
            tag2.setPost(post);
            tagRepository.save(tag1);
            tagRepository.save(tag2);

            post.setTags(List.of(tag1, tag2));
            postRepository.save(post);
//            int k = 1;

//            System.out.println("Id: " + post.getId());
//            System.out.println("Title: " + post.getTitle());
//            System.out.println("User Id: " + post.getAuthor().getId());
//            System.out.println("Content: " + post.getContent());
//            System.out.println("CreatetAt: " + post.getCreatedAt());
        }
    }
}
