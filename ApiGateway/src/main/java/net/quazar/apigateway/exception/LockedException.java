package net.quazar.apigateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.LOCKED)
public class LockedException extends ApiException {
    public LockedException(String timestamp) {
        super(timestamp);
    }

    public LockedException(String message, String timestamp) {
        super(message, timestamp);
    }

    public LockedException(String message, Throwable cause, String timestamp) {
        super(message, cause, timestamp);
    }

    public LockedException(Throwable cause, String timestamp) {
        super(cause, timestamp);
    }
}
