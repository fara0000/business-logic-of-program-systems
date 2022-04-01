package backend.controllers.controllerAdvices;

import backend.exceptions.ServiceDataBaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GodAdvice {

    @ExceptionHandler(ServiceDataBaseException.class)
    public ResponseEntity<String> handleException(ServiceDataBaseException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
