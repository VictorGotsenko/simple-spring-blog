package spring.blog.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Data
public class PostDTO {
    private Long id;
    private String title;
    private String content;
    private boolean published;
    private Long authorId; // ссылка на владельца
    private List<String> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
