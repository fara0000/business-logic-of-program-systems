package backend.dto.responses;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@Builder
public class LoginDto {
    private Long id;
    private String email;
}
