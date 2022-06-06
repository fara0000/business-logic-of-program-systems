package backend.dto.responses;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
@Data
public class PinWithPhotoResponse {
    private Long id;
    private String name;
    private String description;
    private String altText;
    private String link;
    private String photo;
}
