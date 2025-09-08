package spring.blog.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import spring.blog.exception.ResourceNotFoundException;
import spring.blog.model.Post;
import spring.blog.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api")
public class PostsController {

    @Autowired
    private PostRepository postRepository;

    @Value("${app.page-size}")
    int postsPerPage;

    // http get localhost:8080/posts
    // http get localhost:8080/api/posts
    @GetMapping("/posts")
    @ResponseStatus(HttpStatus.OK)
    public List<Post> indexPosts() {
        return postRepository.findAll();
    }

    // {id} - title
    // http get localhost:8080/posts/title01
    // http get localhost:8080/api/posts/title01
    // http get localhost:8080/api/posts/1
    @GetMapping("/posts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Post show(@PathVariable Long id) {
        var post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id:" + id + " Not Found"));
        return post;
    }

    /* Это создание страницы — здесь возвращается информация о добавленной странице
http post localhost:8080/posts title=title01 content=somecontent author=author01
http post localhost:8080/posts title=title02 content=somecontent02 author=author02
http post localhost:8080/posts title=title01 author=author03

http post localhost:8080/api/posts title=title01 content=somecontent author=author01
http post localhost:8080/api/posts title=title02 content=somecontent02 author=author02
http post localhost:8080/api/posts title=title01 author=author03

http post localhost:8080/api/posts title=title01 content=somecontent02
     */
    @PostMapping("/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Post> createPost(@Valid @RequestBody Post post) {
        Post saved = postRepository.save(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // http put localhost:8080/posts/title01 title=title01Up content=somecontentUpdate author=author01
    // http put localhost:8080/api/posts/title01 title=title01Up content=somecontentUpdate author=author01
//    http put localhost:8080/api/posts/2 title=title011 content=somecontent0555
    @PutMapping("/posts/{id}") // Обновление поста
    @ResponseStatus(HttpStatus.OK)
//    public Post update(@Valid @NotBlank @PathVariable Long id, @RequestBody Post data) {
    public Post update(@Valid @PathVariable Long id, @RequestBody Post data) {
        List<Post> posts = postRepository.findAll();
        for (final Post post : posts) {
            if (data.equals(post)) {
                throw new ResourceNotFoundException("Post with id:" + id + " Not Found");
            }
        }


        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id:" + id + " Not Found"));
        post.setTitle(data.getTitle());
        post.setContent(data.getContent());
        post.setPublished(data.isPublished());
        postRepository.save(post);
        return post;
    }

    // http delete localhost:8080/posts/title02
    // http delete localhost:8080/api/posts/title02
    @DeleteMapping("/posts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@NotBlank @PathVariable Long id) {
        postRepository.deleteById(id);
    }
}
