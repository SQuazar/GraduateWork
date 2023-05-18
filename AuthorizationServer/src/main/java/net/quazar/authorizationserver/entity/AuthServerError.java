package net.quazar.authorizationserver.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AuthServerError {
    private int status;
    private String message;
    private LocalDateTime timestamp;
}
