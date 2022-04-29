package backend.dto.responses;

import backend.dto.requests.UserDto;
import backend.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String userToken;
    private LoginDto user;
}
