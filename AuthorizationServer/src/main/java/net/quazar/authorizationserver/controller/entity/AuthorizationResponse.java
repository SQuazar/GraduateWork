package net.quazar.authorizationserver.controller.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthorizationResponse {
    private String message;
    private String accessToken;
    private String refreshToken;
}
