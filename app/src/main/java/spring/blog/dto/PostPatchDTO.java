package spring.blog.dto;

import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class PostPatchDTO {
    private JsonNullable<String> title = JsonNullable.undefined();
    private JsonNullable<String> content = JsonNullable.undefined();
    private JsonNullable<Boolean> published = JsonNullable.undefined();
    private JsonNullable<Long> authorId = JsonNullable.undefined();
//    private Long ; // ссылка на владельца
}
