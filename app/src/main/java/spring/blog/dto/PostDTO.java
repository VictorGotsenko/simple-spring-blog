package spring.blog.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
@Getter
public class PostDTO {
    private Long id;
    private String title;
    private String content;
    private boolean published;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
}
