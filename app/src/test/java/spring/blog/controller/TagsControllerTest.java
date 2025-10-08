package spring.blog.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import spring.blog.mapper.TagMapper;
import spring.blog.model.Post;
import spring.blog.model.Tag;
import spring.blog.model.User;
import spring.blog.repository.PostRepository;
import spring.blog.repository.TagRepository;
import spring.blog.repository.UserRepository;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class TagsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private Faker faker;

    private User testUser;
    private Post testPost;
    private Tag testTag;


    /**
     * Это описание класса, который выполняет определенные действия.
     *
     * @author Имя автора
     * @version 1.0
     */
    @BeforeEach
    public void setUp() {
        testUser = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .supply(Select.field(User::getFirstName), () -> faker.name().firstName())
                .supply(Select.field(User::getLastName), () -> faker.name().lastName())
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .ignore(Select.field(User::getPosts))
                .create();
        userRepository.save(testUser);


        testPost = new Post();
        testPost.setTitle(faker.book().title());
        testPost.setContent(faker.lorem().sentence(1));
        testPost.setAuthor(testUser);
        postRepository.save(testPost);

//        testPost = Instancio.of(Post.class)
//                .ignore(Select.field(Post::getId))
//                .supply(Select.field(Post::getTitle), () -> faker.gameOfThrones().house())
//                .supply(Select.field(Post::getContent), () -> faker.gameOfThrones().quote())
//                .set(Select.field(Post::getAuthor), testUser)
//                .create();
//        postRepository.save(testPost);


        testTag = new Tag();
        testTag.setName(faker.gameOfThrones().house());
        testTag.setPost(testPost);
        tagRepository.save(testTag);

        Tag tag1 = new Tag();
        tag1.setName(faker.gameOfThrones().house());
        tag1.setPost(testPost);
        Tag tag2 = new Tag();
        tag2.setName(faker.gameOfThrones().house());
        tag2.setPost(testPost);
        tagRepository.save(tag1);
        tagRepository.save(tag2);

        testPost.setTags(List.of(tag1, tag2));
        postRepository.save(testPost);

    }

    @Test
    public void testIndex() throws Exception {

        var result = mockMvc.perform(get("/api/tags"))
                .andExpect(status().isOk())
                .andReturn();

        // Тело это строка, в этом случае JSON
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    //получение по id,
    @Test
    void testShow() throws Exception {
        tagRepository.save(testTag);

        var result = mockMvc.perform(get("/api/tags/" + testTag.getId()))
                .andExpect(status().isOk())
                .andReturn(); //Find

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).node("name").isEqualTo(testTag.getName());
    }

    // создание
    //create http post localhost:8080/api/posts title=title10 content=somecontent author=author03 published=true
    @Test
    public void testCreate() throws Exception {
        postRepository.save(testPost);

        var data = new HashMap<>();
        data.put("name", faker.gameOfThrones().house());
        data.put("postId", testPost.getId());

        var request = post("/api/tags") //posts"
                .contentType(MediaType.APPLICATION_JSON)
                // ObjectMapper конвертирует Map в JSON
                .content(om.writeValueAsString(data));

        var result = mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn(); //CREATED

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).node("name").isEqualTo(data.get("name"));
    }

    @Test
    public void testUpdate() throws Exception {

        postRepository.save(testPost);
        tagRepository.save(testTag);

        var data = new HashMap<>();
        data.put("name", "NewTag");
        data.put("postId", testPost.getId());

        var request = put("/api/tags/" + testTag.getId())
                .contentType(MediaType.APPLICATION_JSON)
                // ObjectMapper конвертирует Map в JSON
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        testTag = tagRepository.findById(testTag.getId()).get();
        assertThat(testTag.getName()).isEqualTo(("NewTag"));
    }

    // удаление.
    @Test
    public void testDeleteById() throws Exception {
        tagRepository.save(testTag);

        mockMvc.perform(delete("/api/tags/" + testTag.getId()))
                .andExpect(status().isNoContent());
    }
}
