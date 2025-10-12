package spring.blog.service;


import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import spring.blog.dto.PostCreateDTO;
import spring.blog.dto.PostDTO;
import spring.blog.dto.PostPatchDTO;
import spring.blog.dto.PostUpdateDTO;
import spring.blog.exception.ResourceNotFoundException;
import spring.blog.mapper.PostMapper;
import spring.blog.model.Post;
import spring.blog.repository.PostRepository;
import spring.blog.repository.UserRepository;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostMapper postMapper;

    /**
     * getAll.
     *
     * @return PostDTO
     */
    public List<PostDTO> getAll() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::toDTO)
                .toList();
    }

    /**
     * findById.
     *
     * @param id id
     * @return PostDTO
     */
    public PostDTO findById(Long id) {
        var post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: " + id));
        var postDTO = postMapper.toDTO(post);
        return postDTO;
    }

    /**
     * create.
     *
     * @param postData PostCreateDTO
     * @return PostDTO
     */
    public PostDTO create(PostCreateDTO postData) {
        var user = userRepository.findById(postData.getAuthorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Post post = postMapper.toEntity(postData);
        post.setAuthor(user);
        postRepository.save(post);
        return postMapper.toDTO(post);
    }

    /**
     * @param postData PostUpdateDTO
     * @param id
     * @return PostDTO
     */
    public PostDTO update(PostUpdateDTO postData, Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var author = userRepository.findById(postData.getAuthorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        postMapper.updateEntityFromDTO(postData, post);
        post.setAuthor(author);
        postRepository.save(post);

        return postMapper.toDTO(post);
    }

    /**
     * @param postData PostPatchDTO
     * @param id
     * @return PostDTO
     */
    public PostDTO patchPost(PostPatchDTO postData, Long id) {
        var post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        postData.getTitle().ifPresent(post::setTitle);
        postData.getContent().ifPresent(post::setContent);
        postData.getPublished().ifPresent(post::setPublished);
        if (postData.getAuthorId().isPresent()) {
            JsonNullable<Long> nullableId = JsonNullable.of(postData.getAuthorId().get());
            var author = userRepository.findById(nullableId.get())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
            post.setAuthor(author);
        }
        postRepository.save(post);
        return postMapper.toDTO(post);
    }

    /**
     * @param id
     */
    public void delete(Long id) {
        postRepository.deleteById(id);
    }
}
