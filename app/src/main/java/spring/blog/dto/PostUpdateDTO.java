package spring.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import spring.blog.model.Tag;

import java.util.List;


public class PostUpdateDTO {

    @NotBlank
    @Size(min = 5, max = 100)
    private String title;

    @NotBlank
    @Size(min = 10, max = 1000)
    private String content;

    private Long authorId; // ссылка на владельца

    private boolean published;

    /**
     *
     * @return isPublished
     */
    public boolean isPublished() {
        return published;
    }

    /**
     *
     * @param published
     */
    public void setPublished(boolean published) {
        this.published = published;
    }

    private List<Tag> tags;

    /**
     *
     * @return tags tags
     */
    public List<Tag> getTags() {
        return tags;
    }

    /**
     *
     * @param tags tags
     */
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    /**
     *
     * @return authorId authorId
     */
    public Long getAuthorId() {
        return authorId;
    }

    /**
     *
     * @param authorId authorId
     */
    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    /**
     * Это функция.
     *
     * @return the name of the object
     * @author Имя автора
     */
    public String getTitle() {
        return title;
    }

    /**
     * Это функция.
     *
     * @param title string
     * @author Имя автора
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Это функция.
     *
     * @return the name of the object
     * @author Имя автора
     */
    public String getContent() {
        return content;
    }

    /**
     * Это функция.
     *
     * @param content string
     * @author Имя автора
     */
    public void setContent(String content) {
        this.content = content;
    }
}
