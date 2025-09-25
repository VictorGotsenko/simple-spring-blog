package spring.blog.mapper;

import org.springframework.stereotype.Component;
import spring.blog.dto.PostDTO;
import spring.blog.model.Post;

@Component
public class PostMapper {

    /**
     * Это DTO.
     *
     * @param post post
     * @return dto
     */
    public PostDTO toDTO(Post post) {
        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setPublished(post.isPublished());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        dto.setUserId(post.getUserId());
        return dto;
    }
}
