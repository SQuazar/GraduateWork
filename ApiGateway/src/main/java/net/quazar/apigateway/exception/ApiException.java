package net.quazar.apigateway.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private final String timestamp;

    public ApiException(String timestamp) {
        this.timestamp = timestamp;
    }

    public ApiException(String message, String timestamp) {
        super(message);
        this.timestamp = timestamp;
    }

    public ApiException(String message, Throwable cause, String timestamp) {
        super(message, cause);
        this.timestamp = timestamp;
    }

    public ApiException(Throwable cause, String timestamp) {
        super(cause);
        this.timestamp = timestamp;
    }
}
