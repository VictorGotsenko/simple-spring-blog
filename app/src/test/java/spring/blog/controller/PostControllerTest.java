package spring.blog.controller;


import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import spring.blog.model.Post;
import spring.blog.repository.PostRepository;

import org.instancio.Instancio;
import org.instancio.Select;

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
    private ObjectMapper om;


    // создание
    //create http post localhost:8080/api/posts title=title10 content=somecontent author=author03 published=true
    @Test
    public void testCreatePost() throws Exception {

        var data = new HashMap<>();
        data.put("title", faker.book().title());
        data.put("content", faker.lorem().sentence(1));
        data.put("published", false);

//        data.put("userId", faker.random().nextInt(5));

        var request = post("/api/posts") //posts"
                .contentType(MediaType.APPLICATION_JSON)
                // ObjectMapper конвертирует Map в JSON
                .content(om.writeValueAsString(data));

        var result = mockMvc.perform(request)
//                .andExpect(status().isOk())
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
        var post = Instancio.of(Post.class)
                .ignore(Select.field(Post::getId))
                .supply(Select.field(Post::getTitle), () -> faker.book().title())
                .supply(Select.field(Post::getContent), () -> faker.lorem().sentence(1))
                .supply(Select.field(Post::getUserId), () -> (long) faker.random().nextInt(5))
                .create();


        postRepository.save(post);

        var result = mockMvc.perform(get("/api/posts/" + post.getId()))
                .andExpect(status().isOk())
                .andReturn(); //Find

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).node("title").isEqualTo(post.getTitle());
    }

    // обновление,
    @Test
    public void testUpdate() throws Exception {
        var post = Instancio.of(Post.class)
                .ignore(Select.field(Post::getId))
                .supply(Select.field(Post::getTitle), () -> faker.book().title())
                .supply(Select.field(Post::getContent), () -> faker.lorem().sentence(1))
                .supply(Select.field(Post::getUserId), () -> (long) faker.random().nextInt(5))
                .create();

        postRepository.save(post);

        var data = new HashMap<>();
        data.put("title", "MyTitle");
        data.put("content", "Mycontent123456789");

        var request = put("/api/posts/" + post.getId())
                .contentType(MediaType.APPLICATION_JSON)
                // ObjectMapper конвертирует Map в JSON
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                 .andExpect(status().isOk());

        post = postRepository.findById(post.getId()).get();
        assertThat(post.getTitle()).isEqualTo(("MyTitle"));
    }

    // удаление.
    @Test
    public void testDeleteById() throws Exception {

        var post = Instancio.of(Post.class)
                .ignore(Select.field(Post::getId))
                .supply(Select.field(Post::getTitle), () -> faker.book().title())
                .supply(Select.field(Post::getContent), () -> faker.lorem().sentence(1))
                .create();

        postRepository.save(post);

        mockMvc.perform(delete("/api/posts/" + post.getId()))
                .andExpect(status().isNoContent());
    }
}
