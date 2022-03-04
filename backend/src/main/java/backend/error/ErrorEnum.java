package backend.error;

import backend.error.dto.ApplicationErrorDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorEnum {
    AUTH_PASSWORD_ERROR("Введён некорректный пароль"),
    AUTH_LOGIN_ERROR("Введён некорректный логин"),
    UNAUTHORIZED_ACCESS("Неавторизованный доступ"),
    ACCESS_DENIED("Недостаточно прав для доступа к ресурсу"),
    HANDLER_NOT_FOUND("HANDLER_NOT_FOUND"),
    UNKNOWN_ERROR("Неизвестная ошибка сервера"),
    SERVICE_UNAVAILABLE("Сервис недоступен");

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