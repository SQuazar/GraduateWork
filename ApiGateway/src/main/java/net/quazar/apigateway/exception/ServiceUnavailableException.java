package net.quazar.apigateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class ServiceUnavailableException extends ApiException {
    public ServiceUnavailableException(String timestamp) {
        super(timestamp);
    }

    public ServiceUnavailableException(String message, String timestamp) {
        super(message, timestamp);
    }

    public ServiceUnavailableException(String message, Throwable cause, String timestamp) {
        super(message, cause, timestamp);
    }

    public ServiceUnavailableException(Throwable cause, String timestamp) {
        super(cause, timestamp);
    }
}
