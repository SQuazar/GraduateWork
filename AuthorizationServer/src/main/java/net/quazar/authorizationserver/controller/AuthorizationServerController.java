package net.quazar.authorizationserver.controller;

import lombok.AllArgsConstructor;
import net.quazar.authorizationserver.controller.entity.AuthorizationResponse;
import net.quazar.authorizationserver.controller.entity.TokenResponse;
import net.quazar.authorizationserver.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class AuthorizationServerController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthorizationResponse> login(@RequestParam String username, @RequestParam String password) {
        return ResponseEntity.ok(authenticationService.login(username, password));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<TokenResponse> refreshToken(@RequestParam("refresh_token") String refreshToken) {
        return ResponseEntity.ok(authenticationService.refreshToken(refreshToken));
    }
}
