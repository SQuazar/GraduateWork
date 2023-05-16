package net.quazar.resourceserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TelegramUserNotFoundException extends RuntimeException {
    public TelegramUserNotFoundException() {
    }

    public TelegramUserNotFoundException(String message) {
        super(message);
    }

    public TelegramUserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TelegramUserNotFoundException(Throwable cause) {
        super(cause);
    }
}
