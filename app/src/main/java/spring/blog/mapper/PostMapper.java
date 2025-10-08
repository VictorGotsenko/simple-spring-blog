package spring.blog.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import spring.blog.dto.PostCreateDTO;
import spring.blog.dto.PostDTO;
import spring.blog.dto.PostUpdateDTO;
import spring.blog.model.Post;
import spring.blog.model.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)

//public interface PostMapper {
//    PostDTO toDTO(Post post);
//    Post toEntity(PostCreateDTO dto);
//    void updateEntityFromDTO(PostUpdateDTO dto, @MappingTarget Post post);
//}
/*
В документации MapStruct показаны примеры с интерфейсом, а не абстрактным классом.
Технически эта библиотека работает и с интерфейсами, и абстрактными классами.
Использовать последние удобнее, потому что в абстрактные классы можно сделать инъекцию зависимостей,
если это необходимо.
 */

public abstract class PostMapper {
    public abstract Post toEntity(PostCreateDTO dto);

    @Mapping(target = "authorId", source = "post", qualifiedByName = "userGetId")
    @Mapping(target = "tags", source = "post", qualifiedByName = "tagsGet")
    public abstract PostDTO toDTO(Post post);

    public abstract void updateEntityFromDTO(PostUpdateDTO dto, @MappingTarget Post model);

    /**
     * @param post
     * @return userId userId
     */
    @Named("userGetId")
    public Long userGetId(Post post) {
        //add your custom mapping implementation
        return post.getAuthor().getId();
    }

    /**
     * @param post
     * @return Tags
     */
    @Named("tagsGet")
    public List<String> tagsGet(Post post) {
        List<String> result = new ArrayList<>();
        return post.getTags().stream()
                .map(Tag::getName)
                .collect(Collectors.toList());
    }

}



