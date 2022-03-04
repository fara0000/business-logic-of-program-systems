package backend.error;

import backend.error.dto.ApplicationErrorDto;
import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {
    private final ApplicationErrorDto error;

    public ApplicationException(ApplicationErrorDto error) {
        super(error.getMessage());
        this.error = error;
    }

    public ApplicationErrorDto getError() {
        return this.error;
    }
}
