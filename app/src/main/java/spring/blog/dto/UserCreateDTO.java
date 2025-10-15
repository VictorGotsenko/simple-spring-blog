package spring.blog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserCreateDTO {
    //firstName=Alise lastName=Fox email=alise@mail.io birthday=2001-01-10
    @NotBlank
    @Size(min = 2, max = 50)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 50)
    private String lastName;
//
//    @NotBlank
//    @DateTimeFormat(pattern = "dd-MM-yyyy")
//    private LocalDate birthday;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String passwordDigest;
}
