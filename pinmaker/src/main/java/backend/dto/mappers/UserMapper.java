package backend.dto.mappers;
import backend.dto.responses.LoginDto;
import backend.entities.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    public LoginDto convertMemberToDto(User user) {
        return LoginDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .build();
    }
}
