package spring.blog.controller.api;


import jakarta.validation.Valid;
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
import org.springframework.web.server.ResponseStatusException;
import spring.blog.dto.TagCreateDTO;
import spring.blog.dto.TagDTO;
import spring.blog.dto.TagUpdateDTO;
import spring.blog.mapper.TagMapper;
import spring.blog.model.Post;
import spring.blog.model.Tag;
import spring.blog.repository.PostRepository;
import spring.blog.repository.TagRepository;
import spring.blog.repository.UserRepository;


import java.util.List;

@Validated
@RestController
@RequestMapping("/api/tags")
public class TagsController {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    public TagsController(TagRepository tagRepository,
                          TagMapper tagMapper,
                          UserRepository userRepository,
                          PostRepository postRepository) {

        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }



    /**
     * index.
     *
     * @return List<TagDTO>
     */
    @GetMapping("")
    public List<TagDTO> tagIndex() {
        return tagRepository.findAll()
                .stream()
                .map(tagMapper::toDTO)
                .toList();
    }


    /**
     * show {id}.
     * Это функция GET id.
     *
     * @param id id
     * @return the name of the Post
     */
    // http get localhost:8080/api/posts/1
    @GetMapping("/{id}")
    public ResponseEntity<TagDTO> showTag(@PathVariable Long id) {
        return tagRepository.findById(id)
                .map(tag -> ResponseEntity.ok(tagMapper.toDTO(tag)))
                .orElse(ResponseEntity.notFound().build());
    }


    /**
     * create.
     *
     * @param dto dto
     * @return TagDTO
     */
    @PostMapping("")
    public ResponseEntity<TagDTO> create(@Valid @RequestBody TagCreateDTO dto) {
        var post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        Tag tag = tagMapper.toEntity(dto);
        tag.setPost(post);
        tagRepository.save(tag);
        return ResponseEntity.status(HttpStatus.CREATED).body(tagMapper.toDTO(tag));
    }

    /**
     * update.
     *
     * @param id
     * @param dto
     * @return TagDTO
     */
    @PutMapping("/{id}") // Обновление поста
    public ResponseEntity<TagDTO> update(@PathVariable Long id,
                                         @Valid @RequestBody TagUpdateDTO dto) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found"));
        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        tagMapper.updateEntityFromDTO(dto, tag);
        tag.setPost(post);
        tagRepository.save(tag);

        return ResponseEntity.ok(tagMapper.toDTO(tag));
    }


    /**
     * delete.
     *
     * @param id
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        tagRepository.deleteById(id);
    }
}
