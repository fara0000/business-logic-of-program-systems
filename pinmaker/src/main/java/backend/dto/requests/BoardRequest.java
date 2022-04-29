package backend.dto.requests;

import lombok.Data;

@Data
public class BoardRequest {
    private String name;
    private boolean isPublic;
    private Long userId;
}

