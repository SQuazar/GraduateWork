package net.quazar.apigateway.exception;

import net.quazar.apigateway.entity.ApiError;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiGatewayExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Object> handleApiException(ApiException ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ResponseStatus rs;
        if ((rs = AnnotationUtils.getAnnotation(ex.getClass(), ResponseStatus.class)) != null)
            status = rs.value();
        ApiError apiError = ApiError.builder()
                .message(ex.getMessage())
                .status(status.value())
                .timestamp(ex.getTimestamp())
                .build();
        return new ResponseEntity<>(apiError, status);
    }
}
