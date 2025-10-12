package spring.blog.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
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
import spring.blog.dto.PostCreateDTO;
import spring.blog.dto.PostDTO;
import spring.blog.dto.PostPatchDTO;
import spring.blog.dto.PostUpdateDTO;
import spring.blog.mapper.PostMapper;
import spring.blog.repository.PostRepository;
import spring.blog.repository.UserRepository;
import spring.blog.service.PostService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/posts")
public class PostsController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;

    @Autowired
    private PostService postService;


    public PostsController(PostRepository postRepository,
                           UserRepository userRepository,
                           PostMapper postMapper) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postMapper = postMapper;
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
     * @return the name of the Page<Post>
     */
    // http get localhost:8080/api/posts?page=0&size=3
    // curl GET 'localhost:8080/api/posts?page=0&size=3&sort=createdAt,desc'
    // http get 'localhost:8080/api/posts?page=0&size=3&sort=createdAt,desc'
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<PostDTO>> index() {
        var posts = postService.getAll();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(posts.size()))
                .body(posts);
    }
//    @ResponseStatus(HttpStatus.OK)
//    public Page<Post> getPublishedPosts(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(defaultValue = "createdAt,desc") String sortParam) {
//
//        Sort sort = Sort.by(Sort.Direction.fromString(sortParam.split(",")[1]), sortParam.split(",")[0]);
//        Pageable pageable = PageRequest.of(page, size, sort);
//        return postRepository.findByPublishedTrue(pageable);
//    }

    /**
     * Это функция GET Id.
     *
     * @param id id
     * @return PostDTO
     */
    // {id} - title
    // http get localhost:8080/api/posts/1
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PostDTO> show(@PathVariable Long id) {
        var post = postService.findById(id);
        return ResponseEntity.ok()
//                .header("X-Total-Count", String.valueOf(post.))
                .body(post);
    }

    /* Это создание страницы — здесь возвращается информация о добавленной странице

http post localhost:8080/api/posts title=title01 content=somecontent userId=1 published=true
http post localhost:8080/api/posts title=title011 content=somecontent author=author02 published=true
http post localhost:8080/api/posts title=title03 content=somecontent author=author01 published=false
http post localhost:8080/api/posts title=title04 content=somecontent author=author05 published=true
http post localhost:8080/api/posts title=title05 content=somecontent author=author05 published=false
http post localhost:8080/api/posts title=title07 content=somecontent author=author03 published=true

http post localhost:8080/api/posts title=title01 content=somecontent123456789  published=true authorId=1
     */

    /**
     * Это функция POST Create.
     *
     * @param dto data
     * @return the name of the Post
     */
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PostDTO> create(@Valid @RequestBody PostCreateDTO dto) {
        var post = postService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
//                .header("X-Total-Count", String.valueOf(post.))
                .body(post);
    }

    /**
     * Это функция PUT.
     *
     * @param id  id
     * @param dto data
     * @return the name of the Post
     */
    // change whole object Post
    //  http put localhost:8080/api/posts/2 title=title011 content=somecontent0555  published=true authorId=1
    @PutMapping("/{id}") // Обновление поста
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PostDTO> update(@PathVariable Long id,
                                          @Valid @RequestBody PostUpdateDTO dto) {
        var post = postService.update(dto, id);
        return ResponseEntity.status(HttpStatus.CREATED)
//                .header("X-Total-Count", String.valueOf(post.))
                .body(post);
    }


    /**
     * Это функция PATCH.
     *
     * @param id  id
     * @param dto data
     * @return the name of the Post
     */
    // Change only present fields
    //  http patch localhost:8080/api/posts/2 title=title_Changed published=true
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PostDTO> patchPost(@PathVariable Long id,
                                             @RequestBody PostPatchDTO dto) {
        var post = postService.patchPost(dto, id);
        return ResponseEntity.status(HttpStatus.CREATED)
//                .header("X-Total-Count", String.valueOf(post.))
                .body(post);
    }

    /**
     * Это функция DELETE.
     *
     * @param id id
     */
    // http delete localhost:8080/api/posts/2
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        postService.delete(id);
    }
}
