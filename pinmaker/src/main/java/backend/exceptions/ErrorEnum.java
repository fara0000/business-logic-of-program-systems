package backend.exceptions;

import backend.exceptions.dto.ApplicationErrorDto;
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
    SERVICE_UNAVAILABLE("Сервис недоступен"),
    UNAUTHORIZED_EXCEPTION("Неверные учетные данные пользователя"),
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
