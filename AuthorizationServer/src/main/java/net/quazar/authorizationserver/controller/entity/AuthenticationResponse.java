package net.quazar.authorizationserver.controller.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationResponse {
    private String message;
    private String token;
    @JsonProperty("refresh_token")
    private String refreshToken;
}
