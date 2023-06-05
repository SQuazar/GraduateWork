package net.quazar.authorizationserver.exception;

import net.quazar.authorizationserver.entity.AuthServerError;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class AuthorizationServerExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<AuthServerError> handle(RuntimeException e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ResponseStatus responseStatus;
        if ((responseStatus = AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class)) != null)
            status = responseStatus.value();
        return ResponseEntity.status(status)
                .body(AuthServerError.builder()
                        .status(status.value())
                        .message(e.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<AuthServerError> handle(BadCredentialsException e) {
        return ResponseEntity.status(403)
                .body(AuthServerError.builder()
                        .status(403)
                        .message("Неверный логин или пароль")
                        .timestamp(LocalDateTime.now())
                        .build());
    }
}
