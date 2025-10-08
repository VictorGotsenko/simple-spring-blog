package spring.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TagUpdateDTO {

    @NotBlank
    @Size(min = 5, max = 100)
    private String name;

    private Long postId; // ссылка на пост, где расположен тег

    /**
     *
     * @return name
     */
    public @NotBlank @Size(min = 5, max = 100) String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(@NotBlank @Size(min = 5, max = 100) String name) {
        this.name = name;
    }

    /**
     *
     * @return postId
     */
    public Long getPostId() {
        return postId;
    }

    /**
     *
     * @param postId
     */
    public void setPostId(Long postId) {
        this.postId = postId;
    }
}
