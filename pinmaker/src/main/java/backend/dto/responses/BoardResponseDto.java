package backend.dto.responses;

import lombok.Data;

import java.util.List;

@Data
public class BoardResponseDto {
    private Long id;
    private String name;
    private List<PinResponseDto> pins;
    private boolean is_blocked;
}
