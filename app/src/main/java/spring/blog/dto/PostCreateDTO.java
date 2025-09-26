package spring.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PostCreateDTO {

    @NotBlank
    @Size(min = 3, max = 100)
    private String title;

    @NotBlank
    @Size(min = 10)
    private String content;

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
