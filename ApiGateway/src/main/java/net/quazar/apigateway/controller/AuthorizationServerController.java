package net.quazar.apigateway.controller;

import lombok.AllArgsConstructor;
import net.quazar.apigateway.proxy.AuthorizationServerProxy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthorizationServerController {
    private final AuthorizationServerProxy authorizationServerProxy;

    @PostMapping("/login")
    public AuthorizationServerProxy.AuthenticationResponse login(@RequestParam String username, @RequestParam String password) {
        return authorizationServerProxy.login(username, password);
    }

    @PostMapping("/refreshToken")
    public AuthorizationServerProxy.TokenResponse refreshToken(@CookieValue("refresh_token") String refreshToken) {
        return authorizationServerProxy.refreshToken(refreshToken);
    }

    @PostMapping("/verifyToken")
    public AuthorizationServerProxy.VerifyTokenResponse verifyToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        return authorizationServerProxy.verifyToken(authorization);
    }

    @GetMapping("/check")
    public ResponseEntity<?> check() {
        return ResponseEntity.ok().build();
    }

}
