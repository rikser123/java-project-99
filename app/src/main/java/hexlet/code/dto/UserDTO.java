package hexlet.code.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.time.LocalDate;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate createdAt;

    @Getter
    @Setter
    public static class UserUpdateDto {
        private JsonNullable<String> email;
        private JsonNullable<String> firstName;
        private JsonNullable<String> lastName;
        private JsonNullable<String> password;
    }

    @Getter
    @Setter
    public static class UserCreateDTO {
        @NotBlank
        @Email
        private String email;

        @NotBlank
        private String firstName;

        @NotBlank
        private String lastName;

        @NotBlank
        private String password;
    }
}
