package net.quazar.apigateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class BadUserCredentialsException extends ApiException {
    public BadUserCredentialsException(String timestamp) {
        super(timestamp);
    }

    public BadUserCredentialsException(String message, String timestamp) {
        super(message, timestamp);
    }

    public BadUserCredentialsException(String message, Throwable cause, String timestamp) {
        super(message, cause, timestamp);
    }

    public BadUserCredentialsException(Throwable cause, String timestamp) {
        super(cause, timestamp);
    }
}
