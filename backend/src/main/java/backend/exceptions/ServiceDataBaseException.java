package backend.exceptions;

public class ServiceDataBaseException extends RuntimeException{
    private String message;

    public ServiceDataBaseException(String message) {
        super(message);
        this.message = message;
    }

    public ServiceDataBaseException() {
    }
}
