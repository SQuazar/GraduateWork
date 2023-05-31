package net.quazar.authorizationserver.service;

import net.quazar.authorizationserver.controller.entity.AuthorizationResponse;
import net.quazar.authorizationserver.controller.entity.TokenResponse;
import net.quazar.authorizationserver.controller.entity.VerifyTokenResponse;
import org.springframework.lang.NonNull;

public interface AuthenticationService {
    AuthorizationResponse login(@NonNull String username, @NonNull String password);
    TokenResponse refreshToken(@NonNull String refreshToken);
    VerifyTokenResponse verifyAccessToken(@NonNull String accessToken);
}
