package spring.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TagCreateDTO {
    @NotBlank
    @Size(min = 2, max = 50)
    private String name;

    private Long postId; // ссылка на пост, где расположен тег

    /**
     *
     * @return name
     */
    public @NotBlank @Size(min = 2, max = 50) String getName() {
        return name;
    }

    /**
     *
     * @param name name
     */
    public void setName(@NotBlank @Size(min = 2, max = 50) String name) {
        this.name = name;
    }

    /**
     *
     * @return postId postId
     */
    public Long getPostId() {
        return postId;
    }

    /**
     *
     * @param postId postId
     */
    public void setPostId(Long postId) {
        this.postId = postId;
    }
}
