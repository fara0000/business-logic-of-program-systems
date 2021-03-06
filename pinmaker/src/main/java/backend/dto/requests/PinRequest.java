package backend.dto.requests;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PinRequest {
    private String name;
    private Long board_id;
    private String description;
    private String alt_text;
    private String link;
    private Long userId;
    private String fileName;
}
