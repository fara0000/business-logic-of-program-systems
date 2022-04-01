package backend.exceptions.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class ApplicationErrorDto {

    private String message;

    private LocalDateTime time;

    public static ApplicationErrorDto of(String message) {
        return of(message, LocalDateTime.now());
    }
}

