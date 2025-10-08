package spring.blog.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import spring.blog.dto.TagCreateDTO;
import spring.blog.dto.TagDTO;
import spring.blog.dto.TagUpdateDTO;
import spring.blog.model.Tag;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TagMapper {

    public abstract Tag toEntity(TagCreateDTO dto);

    @Mapping(target = "postId", source = "tag", qualifiedByName = "tagGetId")
    public abstract TagDTO toDTO(Tag tag);

    public abstract void updateEntityFromDTO(TagUpdateDTO dto, @MappingTarget Tag model);

    /**
     *
     * @param tag
     * @return post.getId
     */
    @Named("tagGetId")
    public Long tagGetId(Tag tag) {
        //add your custom mapping implementation
        return tag.getPost().getId();
    }
}
