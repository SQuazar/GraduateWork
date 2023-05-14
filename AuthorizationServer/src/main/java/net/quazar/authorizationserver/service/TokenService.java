package net.quazar.authorizationserver.service;

import net.quazar.authorizationserver.entity.Token;
import net.quazar.authorizationserver.entity.User;
import net.quazar.authorizationserver.exception.TokenNotFoundException;
import org.springframework.lang.NonNull;

import java.util.List;

public interface TokenService {
    Token saveToken(@NonNull String token, @NonNull User user);
    Token saveToken(@NonNull String token, @NonNull Token.TokenType type, @NonNull User user);
    Token getByToken(@NonNull String token) throws TokenNotFoundException;
    void deleteToken(@NonNull String token);
    List<Token> getByUser(@NonNull User user) throws TokenNotFoundException;
    List<Token> getExpiredTokens();
    List<Token> getRevokedTokens();
    int getExpiredTokensCount();
    int getRevokedTokensCount();
    void deleteExpiredTokens();
    void deleteRevokedTokens();
    void deleteTokens(List<Token> delete);
}
