package spring.blog.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import spring.blog.exception.ResourceNotFoundException;
import spring.blog.model.Post;
import spring.blog.repository.PostRepository;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api")
public class PostsController {

    private final PostRepository postRepository;

    public PostsController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Value("${app.page-size}")
    int postsPerPage;

    // curl -X GET 'http://localhost:8080/api/test?param1=1&param2=2'
    // строку передавать - кавычки

    /**
     * Это функция GET.
     *
     * @param param1 test1
     * @param param2 test2
     * @return the name of the object
     * @author Имя автора
     * @version 1.0
     */
    @GetMapping("/test")
    public String getRequestParam(
            @RequestParam(name = "param1") Integer param1,
            @RequestParam(name = "param2") Integer param2) {
        return "param1:" + param1 + " param2:" + param2 + "\n";
    }


    /**
     * Это функция GET.
     *
     * @param page      page
     * @param size      size
     * @param sortParam sortParam
     * @return the name of the Page<Post>
     */
    // http get localhost:8080/api/posts?page=0&size=3
    // curl GET 'localhost:8080/api/posts?page=0&size=3&sort=createdAt,desc'
    // http get 'localhost:8080/api/posts?page=0&size=3&sort=createdAt,desc'
    @GetMapping("/posts")
    @ResponseStatus(HttpStatus.OK)
    public Page<Post> getPublishedPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sortParam) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortParam.split(",")[1]), sortParam.split(",")[0]);
        Pageable pageable = PageRequest.of(page, size, sort);
        return postRepository.findByPublishedTrue(pageable);
    }


    /**
     * Это функция GET id.
     *
     * @param id id
     * @return the name of the Post
     */
    // {id} - title
    // http get localhost:8080/api/posts/1
    @GetMapping("/posts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Post show(@PathVariable Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id:" + id + " Not Found"));
    }

    /* Это создание страницы — здесь возвращается информация о добавленной странице

http post localhost:8080/api/posts title=title01 content=somecontent author=author01 published=true
http post localhost:8080/api/posts title=title011 content=somecontent author=author02 published=true
http post localhost:8080/api/posts title=title03 content=somecontent author=author01 published=false
http post localhost:8080/api/posts title=title04 content=somecontent author=author05 published=true
http post localhost:8080/api/posts title=title05 content=somecontent author=author05 published=false
http post localhost:8080/api/posts title=title07 content=somecontent author=author03 published=true
http post localhost:8080/api/posts title=title08 content=somecontent author=author02 published=true
http post localhost:8080/api/posts title=title09 content=somecontent author=author02 published=true
http post localhost:8080/api/posts title=title10 content=somecontent author=author03 published=true

     */

    /**
     * Это функция POST Create.
     *
     * @param post data
     * @return the name of the Post
     */
    @PostMapping("/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Post> createPost(@Valid @RequestBody Post post) {
        Post saved = postRepository.save(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * Это функция PUT.
     *
     * @param id id
     * @param data data
     * @return the name of the Post
     */
    //  http put localhost:8080/api/posts/2 title=title011 content=somecontent0555
    @PutMapping("/posts/{id}") // Обновление поста
    @ResponseStatus(HttpStatus.OK)
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

    /**
     * Это функция DELETE.
     *
     * @param id id
     */
    // http delete localhost:8080/api/posts/2
    @DeleteMapping("/posts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        postRepository.deleteById(id);
    }
}
