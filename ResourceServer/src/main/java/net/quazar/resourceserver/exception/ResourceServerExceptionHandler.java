package net.quazar.resourceserver.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

@ControllerAdvice
public class ResourceServerExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResourceServerError> handleException(RuntimeException ex) {
        ResponseStatus status;
        if ((status = ex.getClass().getAnnotation(ResponseStatus.class)) == null)
            return ResponseEntity.status(500)
                    .body(ResourceServerError.builder()
                            .status(500)
                            .message(ex.getMessage())
                            .timestamp(LocalDateTime.now())
                            .build());
        return ResponseEntity.status(status.value())
                .body(ResourceServerError.builder()
                        .status(status.value().value())
                        .message(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @Data
    @Builder
    static class ResourceServerError {
        private int status;
        private String message;
        private LocalDateTime timestamp;
    }
}
