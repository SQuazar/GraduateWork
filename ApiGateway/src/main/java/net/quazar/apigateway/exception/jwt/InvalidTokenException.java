package net.quazar.apigateway.exception.jwt;

import net.quazar.apigateway.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidTokenException extends ApiException {
    public InvalidTokenException(String timestamp) {
        super(timestamp);
    }

    public InvalidTokenException(String message, String timestamp) {
        super(message, timestamp);
    }

    public InvalidTokenException(String message, Throwable cause, String timestamp) {
        super(message, cause, timestamp);
    }

    public InvalidTokenException(Throwable cause, String timestamp) {
        super(cause, timestamp);
    }
}
