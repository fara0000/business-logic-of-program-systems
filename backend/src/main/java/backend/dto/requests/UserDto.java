package backend.dto.requests;

import lombok.Data;

@Data
public class UserDto {
    private String email;
    private String password;
    private String age;
}
