package spring.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import spring.blog.Application;
import spring.blog.model.Post;
import spring.blog.model.User;
import spring.blog.repository.PostRepository;
import spring.blog.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PostsController {

    @Autowired
    private PostRepository postRepository;

    @Value("${app.page-size}")
    int postsPerPage;

    // http get localhost:8080/posts
    // http get localhost:8080/api/posts
//    @GetMapping("/posts")
//    public ResponseEntity<List<Post>> index(@RequestParam(defaultValue = "10" ) Integer postsPerPage) {
//        var result = Application.getPosts()
//                .stream().limit(postsPerPage).toList();
//        return ResponseEntity.ok()
//                .header("X-Total-Count", String.valueOf(Application.getPosts().size()))
//                .body(result);
//    }

    @GetMapping("/posts")
    @ResponseStatus(HttpStatus.OK)
    public List<Post> indexPosts() {
        return postRepository.findAll();
    }


    // {id} - title
    // http get localhost:8080/posts/title01
    // http get localhost:8080/api/posts/title01
    // http get localhost:8080/api/posts/1
//    @GetMapping("/posts/{id}")
//    public ResponseEntity<Post> show(@PathVariable String id) {
//        Optional<Post> post = Application.getPosts().stream()
//                .filter(p -> p.getTitle().equals(id))
//                .findFirst();
//        if (post.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//        return ResponseEntity.of(post);
//    }

    @GetMapping("/posts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Post show(@PathVariable Long id) {
        var post = postRepository.findById(id).get();
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
//    @PostMapping("/posts") // Создание поста
//    public ResponseEntity<Post> create(@RequestBody Post post) {
//        // добавить валидацию: например, чтобы title и content не были пустыми.
//        if ((null == post.getContent()) || (null == post.getTitle())) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(post);
//        }
//        List<Post> posts = Application.getPosts();
//        post.setCreatedAt(LocalDateTime.now());
//        posts.add(post);
//        Application.setPosts(posts);
//        return ResponseEntity.status(HttpStatus.CREATED).body(post);
//    }

    @PostMapping("/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public Post create(@RequestBody Post post) {
        postRepository.save(post);
        return post;
    }

    // http put localhost:8080/posts/title01 title=title01Up content=somecontentUpdate author=author01
    // http put localhost:8080/api/posts/title01 title=title01Up content=somecontentUpdate author=author01
//    http put localhost:8080/api/posts/2 title=title011 content=somecontent0555
//    @PutMapping("/posts/{id}") // Обновление поста
//    public ResponseEntity<Post> update(@PathVariable String id, @RequestBody Post data) {
//        List<Post> posts = Application.getPosts();
//        // валидация: например, чтобы title и content не были пустыми.
//        if ((null == data.getContent()) || (null == data.getTitle())) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(data);
//        }
//        var maybePost = posts.stream()
//                .filter(p -> p.getTitle().equals(id))
//                .findFirst();
//        if (maybePost.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//        if (maybePost.isPresent()) {
//            var post = maybePost.get();
//            post.setTitle(data.getTitle());
//            post.setContent(data.getContent());
//            post.setAuthor(data.getAuthor());
//            LocalDateTime timeStamp = LocalDateTime.now();
//            post.setCreatedAt(timeStamp);
//            data.setCreatedAt(timeStamp);
//        }
//        Application.setPosts(posts);
//        return ResponseEntity.status(HttpStatus.OK).body(data);
//    }

    @PutMapping("/posts/{id}") // Обновление поста
    public ResponseEntity<Post> update(@PathVariable Long id, @RequestBody Post data) {

        Optional<Post> maybePost = postRepository.findById(id); // Поиск сущности по Id;
        if (maybePost.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            return ResponseEntity.of(maybePost);
        }
        var post = maybePost.get();
        post.setTitle(data.getTitle());
        post.setContent(data.getContent());
        post.setPublished(data.isPublished());
        postRepository.save(post);
        return ResponseEntity.status(HttpStatus.OK).body(post);
    }


    // http delete localhost:8080/posts/title02
    // http delete localhost:8080/api/posts/title02
//    @DeleteMapping("/posts/{id}") // Удаление поста
//    public ResponseEntity<Void> destroy(@PathVariable String id) {
//        List<Post> posts = Application.getPosts();
//        posts.removeIf(p -> p.getTitle().equals(id));
//        Application.setPosts(posts);
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//    }
    @DeleteMapping("/posts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        postRepository.deleteById(id);
    }
}
