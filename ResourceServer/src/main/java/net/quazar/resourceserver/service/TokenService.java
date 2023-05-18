package net.quazar.resourceserver.service;

import net.quazar.resourceserver.entity.Token;
import net.quazar.resourceserver.entity.dto.TokenDto;
import net.quazar.resourceserver.exception.TokenNotFoundException;
import org.springframework.lang.NonNull;

import java.util.List;

public interface TokenService {
    TokenDto saveToken(@NonNull String token, int userId);
    TokenDto saveToken(@NonNull String token, @NonNull Token.TokenType type, int userId);
    TokenDto getByToken(@NonNull String token) throws TokenNotFoundException;
    void deleteToken(@NonNull String token);
    List<TokenDto> getByUserId(int userId);
    List<TokenDto> getExpiredTokens();
    List<TokenDto> getRevokedTokens();
    int getExpiredTokensCount();
    int getRevokedTokensCount();
    void deleteExpiredTokens();
    void deleteRevokedTokens();
    void deleteTokens(List<String> tokens);
}
