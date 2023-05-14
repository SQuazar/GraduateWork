package net.quazar.authorizationserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TokenRevokeException extends RuntimeException {
    public TokenRevokeException() {
    }

    public TokenRevokeException(String message) {
        super(message);
    }

    public TokenRevokeException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenRevokeException(Throwable cause) {
        super(cause);
    }
}
