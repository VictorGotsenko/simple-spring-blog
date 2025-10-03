package spring.blog.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Data
public class PostDTO {
    private Long id;
    private String title;
    private String content;
    private boolean published;
    private Long authorId; // ссылка на владельца
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
