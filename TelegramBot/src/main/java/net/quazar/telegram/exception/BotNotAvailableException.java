package net.quazar.telegram.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.LOCKED)
public class BotNotAvailableException extends RuntimeException {
    public BotNotAvailableException() {
    }

    public BotNotAvailableException(String message) {
        super(message);
    }

    public BotNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public BotNotAvailableException(Throwable cause) {
        super(cause);
    }
}
