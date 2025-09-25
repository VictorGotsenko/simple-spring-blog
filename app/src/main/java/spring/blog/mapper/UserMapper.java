package spring.blog.mapper;

import org.springframework.stereotype.Component;
import spring.blog.dto.UserDTO;
import spring.blog.model.User;

@Component
public class UserMapper {

    /**
     * Это DTO.
     *
     * @param user user
     * @return dto
     */
    public UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId()); //private Long id;
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        return dto;
    }
}
