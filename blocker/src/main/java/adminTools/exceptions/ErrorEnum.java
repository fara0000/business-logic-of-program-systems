package adminTools.exceptions;

import adminTools.exceptions.dto.ApplicationErrorDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorEnum {
    UNKNOWN_ERROR("Неизвестная ошибка сервера"),
    SERVICE_UNAVAILABLE("Сервис недоступен"),
    SERVICE_DATA_BASE_EXCEPTION("Ошибка при сохранении в базу данных"),
    OBJECT_DOES_NOT_EXIST("Объект не существует");

    /**
     * Сообщение ошибки.
     */
    private final String message;

    public void throwIfFalse(Boolean condition) {
        if (!condition) {
            throw exception();
        }
    }

    public ApplicationException exception() {
        return new ApplicationException(createApplicationError());
    }

    public ApplicationErrorDto createApplicationError() {
        return ApplicationErrorDto.of(this.message);
    }

    public void throwException() throws ApplicationException {
        throw exception();
    }
}
