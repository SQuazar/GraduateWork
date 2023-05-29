package net.quazar.apigateway.controller;


import lombok.AllArgsConstructor;
import net.quazar.apigateway.entity.ApiResponse;
import net.quazar.apigateway.proxy.ResourceServerProxy;
import net.quazar.apigateway.proxy.resource.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@AllArgsConstructor
@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    private final ResourceServerProxy resourceServerProxy;

    @GetMapping
    public ResponseEntity<ApiResponse> getProfile(Principal principal) {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .code(200)
                        .response(resourceServerProxy.getUserByUsername(principal.getName()))
                        .build()
        );
    }

    @GetMapping("/authorities")
    public ResponseEntity<ApiResponse> getAuthorities(Authentication authentication) {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .code(200)
                        .response(authentication.getAuthorities()
                                .stream()
                                .map(GrantedAuthority::getAuthority)
                                .toList())
                        .build()
        );
    }

    @GetMapping("/roles")
    public ResponseEntity<ApiResponse> getRoles(Principal principal) {
        UserResponse user = resourceServerProxy.getUserByUsername(principal.getName());
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .code(200)
                        .response(resourceServerProxy.getUserRoles(user.id()))
                        .build()
        );
    }

}
