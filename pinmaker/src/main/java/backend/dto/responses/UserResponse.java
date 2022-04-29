package backend.dto.responses;

import lombok.Data;

import java.util.List;

@Data
public class UserResponse {
    Long id;
    String email;
    String age;
    List<BoardResponseDto> boards;
}
