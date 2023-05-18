package net.quazar.apigateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends ApiException {
    public BadRequestException(String timestamp) {
        super(timestamp);
    }

    public BadRequestException(String message, String timestamp) {
        super(message, timestamp);
    }

    public BadRequestException(String message, Throwable cause, String timestamp) {
        super(message, cause, timestamp);
    }

    public BadRequestException(Throwable cause, String timestamp) {
        super(cause, timestamp);
    }
}
