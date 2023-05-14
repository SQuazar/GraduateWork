package net.quazar.apigateway.proxy;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("authorization-server")
public interface AuthorizationServerProxy {
    @PostMapping("/login")
    AuthenticationResponse login(@RequestParam String username, @RequestParam String password);

    @PostMapping("/refreshToken")
    TokenResponse refreshToken(@RequestParam("refresh_token") String refreshToken);

    @PostMapping("/verifyToken")
    VerifyTokenResponse verifyToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization);

    record AuthenticationResponse(String message, String token, @JsonProperty("refresh_token") String refreshToken) {
    }

    record TokenResponse(String token, @JsonProperty("refresh_token") String refreshToken) {
    }

    record VerifyTokenResponse(String message) {
    }
}
