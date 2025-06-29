package ru.mephi.mephiotp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RegisterEmptyFieldException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public RegisterEmptyFieldException(String message) {
        super(message);
    }
}
