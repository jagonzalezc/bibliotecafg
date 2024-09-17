package org.fabricadegenios.excepciones;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class AutenticacionException extends RuntimeException {

    public AutenticacionException(String message) {
        super(message);
    }
}
