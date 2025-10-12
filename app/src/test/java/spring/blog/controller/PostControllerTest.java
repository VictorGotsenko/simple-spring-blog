package spring.blog.controller;


import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import spring.blog.model.Post;
import spring.blog.model.User;
import spring.blog.repository.PostRepository;

import org.instancio.Instancio;
import org.instancio.Select;
import spring.blog.repository.UserRepository;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    private Faker faker = new Faker();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private ObjectMapper om;

    private User testUser;
    private Post testPost;


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
    }

    // создание
    //create http post localhost:8080/api/posts title=title10 content=somecontent author=author03 published=true
    @Test
    public void testCreatePost() throws Exception {

//http post localhost:8080/api/posts title=title01 content=somecontent123456789  published=true authorId=1
        var data = new HashMap<>();
        data.put("title", "title01");
        data.put("content", "somecontent123456789");
        data.put("published", false);
        data.put("authorId", testUser.getId());

        var request = post("/api/posts") //posts"
                .contentType(MediaType.APPLICATION_JSON)
                // ObjectMapper конвертирует Map в JSON
                .content(om.writeValueAsString(data));

        var result = mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn(); //CREATED

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).node("title").isEqualTo(data.get("title"));
    }

    // получение списка
    @Test
    void testIndex() throws Exception {
//        mockMvc.perform(get("/api/posts"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.content").isArray());

        var result = mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andReturn();

        // Тело это строка, в этом случае JSON
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    //получение по id,
    @Test
    void testFindById() throws Exception {

        userRepository.save(testUser);
        postRepository.save(testPost);

        var result = mockMvc.perform(get("/api/posts/" + testPost.getId()))
                .andExpect(status().isOk())
                .andReturn(); //Find

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).node("title").isEqualTo(testPost.getTitle());
    }

    // обновление,
    @Test
    public void testUpdate() throws Exception {

        postRepository.save(testPost);

        var data = new HashMap<>();
        data.put("title", "MyTitle");
        data.put("content", "Mycontent123456789");
        data.put("published", false);
        data.put("authorId", testUser.getId());


        var request = put("/api/posts/" + testPost.getId())
                .contentType(MediaType.APPLICATION_JSON)
                // ObjectMapper конвертирует Map в JSON
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        testPost = postRepository.findById(testPost.getId()).get();
        assertThat(testPost.getTitle()).isEqualTo(("MyTitle"));
    }

    // удаление.
    @Test
    public void testDeleteById() throws Exception {
        postRepository.save(testPost);
        mockMvc.perform(delete("/api/posts/" + testPost.getId()))
                .andExpect(status().isNoContent());
    }
}
