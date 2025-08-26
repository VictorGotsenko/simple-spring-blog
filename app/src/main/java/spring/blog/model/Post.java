package spring.blog.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class Post {
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdAt;

}
