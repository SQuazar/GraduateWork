package net.quazar.apigateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerError extends ApiException {
    public InternalServerError(String timestamp) {
        super(timestamp);
    }

    public InternalServerError(String message, String timestamp) {
        super(message, timestamp);
    }

    public InternalServerError(String message, Throwable cause, String timestamp) {
        super(message, cause, timestamp);
    }

    public InternalServerError(Throwable cause, String timestamp) {
        super(cause, timestamp);
    }
}
