package backend.dto.responses;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginDto {
    private Long id;
    private String email;
}
