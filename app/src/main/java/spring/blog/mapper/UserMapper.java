package spring.blog.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import spring.blog.dto.UserCreateDTO;
import spring.blog.dto.UserDTO;
import spring.blog.dto.UserUpdateDTO;
import spring.blog.model.User;


@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
//public interface UserMapper {
//    UserDTO toDTO(User user);
//    User toEntity(UserCreateDTO dto);
//    void updateEntityFromDTO(UserUpdateDTO dto, @MappingTarget User user);

/*
В документации MapStruct показаны примеры с интерфейсом, а не абстрактным классом.
Технически эта библиотека работает и с интерфейсами, и абстрактными классами.
Использовать последние удобнее, потому что в абстрактные классы можно сделать инъекцию зависимостей,
если это необходимо.
 */

public abstract class UserMapper {

    public abstract User toEntity(UserCreateDTO dto);
    public abstract UserDTO toDTO(User user);
    public abstract void updateEntityFromDTO(UserUpdateDTO dto, @MappingTarget User user);

}
