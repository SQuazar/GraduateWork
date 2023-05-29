package net.quazar.apigateway.exception.jwt;

import net.quazar.apigateway.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TokenExpiredException extends ApiException {
    public TokenExpiredException(String timestamp) {
        super(timestamp);
    }

    public TokenExpiredException(String message, String timestamp) {
        super(message, timestamp);
    }

    public TokenExpiredException(String message, Throwable cause, String timestamp) {
        super(message, cause, timestamp);
    }

    public TokenExpiredException(Throwable cause, String timestamp) {
        super(cause, timestamp);
    }
}
