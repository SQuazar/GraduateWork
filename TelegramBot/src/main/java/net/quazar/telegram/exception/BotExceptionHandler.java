package net.quazar.telegram.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

@ControllerAdvice
public class BotExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<BotError> handleException(RuntimeException ex) {
        ResponseStatus status;
        if ((status = ex.getClass().getAnnotation(ResponseStatus.class)) == null)
            return ResponseEntity.status(500)
                    .body(BotError.builder()
                            .status(500)
                            .message(ex.getMessage())
                            .timestamp(LocalDateTime.now())
                            .build());
        return ResponseEntity.status(status.value())
                .body(BotError.builder()
                        .status(status.value().value())
                        .message(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build());
    }
    @Data
    @Builder
    static class BotError {
        private int status;
        private String message;
        private LocalDateTime timestamp;
    }
}
