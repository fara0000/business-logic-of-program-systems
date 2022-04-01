package backend.dto.requests;

import backend.entities.Role;
import lombok.Data;

@Data
public class UserDto {
    private String email;
    private String password;
    private String age;
    private Role role;
}
