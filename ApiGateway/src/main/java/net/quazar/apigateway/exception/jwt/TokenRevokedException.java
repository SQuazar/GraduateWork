package net.quazar.apigateway.exception.jwt;

import net.quazar.apigateway.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TokenRevokedException extends ApiException {
    public TokenRevokedException(String timestamp) {
        super(timestamp);
    }

    public TokenRevokedException(String message, String timestamp) {
        super(message, timestamp);
    }

    public TokenRevokedException(String message, Throwable cause, String timestamp) {
        super(message, cause, timestamp);
    }

    public TokenRevokedException(Throwable cause, String timestamp) {
        super(cause, timestamp);
    }
}
