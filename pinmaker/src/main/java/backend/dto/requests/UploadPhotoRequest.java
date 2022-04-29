package backend.dto.requests;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadPhotoRequest {
    private MultipartFile multipartFile;
    private Long userId;
}
