package net.quazar.apigateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends ApiException {
    public NotFoundException(String timestamp) {
        super(timestamp);
    }

    public NotFoundException(String message, String timestamp) {
        super(message, timestamp);
    }

    public NotFoundException(String message, Throwable cause, String timestamp) {
        super(message, cause, timestamp);
    }

    public NotFoundException(Throwable cause, String timestamp) {
        super(cause, timestamp);
    }
}
