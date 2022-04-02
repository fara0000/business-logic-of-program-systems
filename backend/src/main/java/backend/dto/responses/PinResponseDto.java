package backend.dto.responses;

import backend.entities.Board;
import backend.entities.Photo;
import backend.entities.User;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;

@Data
public class PinResponseDto {
    private Long id;
    private String name;
    private String description;
    private String altText;
    private String link;
    private String originalFileName;
    private boolean is_blocked;
}
