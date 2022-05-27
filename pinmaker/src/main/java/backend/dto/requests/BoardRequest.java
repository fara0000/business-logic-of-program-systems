package backend.dto.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardRequest {
    private String name;
    private boolean isPublic;
    private Long userId;
}

