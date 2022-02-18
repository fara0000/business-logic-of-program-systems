package backend.dto.requests;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;

@Data
public class PinRequest {
    private MultipartFile multipartFile;
    private String name;
    private String nameOfBoard;
    private String description;
    private String alt_text;
    private String link;
}
