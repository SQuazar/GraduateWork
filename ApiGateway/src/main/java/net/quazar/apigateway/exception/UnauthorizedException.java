package net.quazar.apigateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends ApiException {
    public UnauthorizedException(String timestamp) {
        super(timestamp);
    }

    public UnauthorizedException(String message, String timestamp) {
        super(message, timestamp);
    }

    public UnauthorizedException(String message, Throwable cause, String timestamp) {
        super(message, cause, timestamp);
    }

    public UnauthorizedException(Throwable cause, String timestamp) {
        super(cause, timestamp);
    }
}
