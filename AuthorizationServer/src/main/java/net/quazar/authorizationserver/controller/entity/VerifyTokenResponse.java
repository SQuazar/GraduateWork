package net.quazar.authorizationserver.controller.entity;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class VerifyTokenResponse {
    private String message;
}
