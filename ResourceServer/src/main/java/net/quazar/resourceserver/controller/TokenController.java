package net.quazar.resourceserver.controller;

import lombok.AllArgsConstructor;
import net.quazar.resourceserver.entity.Token;
import net.quazar.resourceserver.entity.dto.TokenDto;
import net.quazar.resourceserver.service.TokenService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/tokens")
public class TokenController {
    private final TokenService tokenService;

    @GetMapping("/{token}")
    public TokenDto getToken(@PathVariable String token) {
        return tokenService.getByToken(token);
    }

    @GetMapping("/expired")
    public List<TokenDto> getExpiredTokens() {
        return tokenService.getExpiredTokens();
    }

    @GetMapping("/revoked")
    public List<TokenDto> getRevokedTokens() {
        return tokenService.getRevokedTokens();
    }

    @GetMapping("/expired/count")
    public int getExpiredTokensCount() {
        return tokenService.getExpiredTokensCount();
    }

    @GetMapping("/revoked/count")
    public int getRevokedTokensCount() {
        return tokenService.getRevokedTokensCount();
    }

    @GetMapping("/user/{userId}")
    public List<TokenDto> getTokensByUserId(@PathVariable int userId) {
        return tokenService.getByUserId(userId);
    }

    @PostMapping
    public TokenDto saveToken(@RequestParam String token, @RequestParam Optional<Token.TokenType> type, @RequestParam int userId) {
        if (type.isPresent())
            return tokenService.saveToken(token, type.get(), userId);
        return tokenService.saveToken(token, userId);
    }

    @DeleteMapping("/{token}")
    public void deleteToken(@PathVariable String token) {
        tokenService.deleteToken(token);
    }

    @DeleteMapping
    public void deleteTokens(@RequestBody List<String> tokens) {
        tokenService.deleteTokens(tokens);
    }

    @DeleteMapping("/expired")
    public void deleteExpiredTokens() {
        tokenService.deleteExpiredTokens();
    }

    @DeleteMapping("/revoked")
    public void deleteRevokedTokens() {
        tokenService.deleteRevokedTokens();
    }
}
