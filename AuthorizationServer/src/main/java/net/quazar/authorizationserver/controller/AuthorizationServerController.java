package net.quazar.authorizationserver.controller;

import lombok.AllArgsConstructor;
import net.quazar.authorizationserver.controller.entity.AuthenticationResponse;
import net.quazar.authorizationserver.controller.entity.TokenResponse;
import net.quazar.authorizationserver.controller.entity.VerifyTokenResponse;
import net.quazar.authorizationserver.entity.Token;
import net.quazar.authorizationserver.exception.InvalidTokenException;
import net.quazar.authorizationserver.service.AuthenticationService;
import net.quazar.authorizationserver.service.TokenService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
public class AuthorizationServerController {
    private final AuthenticationService authenticationService;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestParam String username, @RequestParam String password) {
        return ResponseEntity.ok(authenticationService.login(username, password));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<TokenResponse> refreshToken(@RequestParam("refresh_token") String refreshToken) {
        return ResponseEntity.ok(authenticationService.refreshToken(refreshToken));
    }

    @PostMapping("/verifyToken")
    public ResponseEntity<VerifyTokenResponse> verifyToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        String bearer = "Bearer ";
        if (authorization.length() <= bearer.length())
            throw new InvalidTokenException("Неверный токен");
        String token = authorization.substring(bearer.length());
        return ResponseEntity.ok(authenticationService.verifyAccessToken(token));
    }

    @GetMapping("/token")
    public ResponseEntity<TokenResponse> getToken(@RequestParam String token) {
        Token res = tokenService.getByToken(token);
        return ResponseEntity.ok(TokenResponse.builder()
                .token(res.getToken())
                .build());
    }
}
