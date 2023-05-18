package net.quazar.authorizationserver.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import net.quazar.authorizationserver.config.service.JwtService;
import net.quazar.authorizationserver.entity.Token;
import net.quazar.authorizationserver.entity.User;
import net.quazar.authorizationserver.exception.TokenNotFoundException;
import net.quazar.authorizationserver.repository.TokenRepository;
import net.quazar.authorizationserver.service.TokenService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {
    private final TokenRepository repository;
    private final JwtService jwtService;

    @Override
    public Token saveToken(@NonNull String token, @NonNull User user) {
        return repository.save(Token.builder()
                .token(token)
                .expire(jwtService.extractClaim(token, claims -> claims.getExpiration().getTime()))
                .user(user)
                .build());
    }

    @Override
    public Token saveToken(@NonNull String token, @NonNull Token.TokenType type, @NonNull User user) {
        return repository.save(Token.builder()
                .token(token)
                .expire(jwtService.extractClaim(token, claims -> claims.getExpiration().getTime()))
                .type(type)
                .user(user)
                .build());
    }

    @Override
    public Token getByToken(@NonNull String token) throws TokenNotFoundException {
        return repository.findByToken(token).orElseThrow(
                () -> new TokenNotFoundException("Данный токен не существует.")
        );
    }

    @Override
    public void deleteToken(@NonNull String token) {
        repository.deleteByToken(token);
    }

    @Override
    public List<Token> getByUser(@NonNull User user) throws TokenNotFoundException {
        return repository.findAllByUser(user);
    }

    @Override
    public List<Token> getExpiredTokens() {
        return repository.findAll().stream()
                .filter(token -> token.getExpire() < System.currentTimeMillis())
                .toList();
    }

    @Override
    public List<Token> getRevokedTokens() {
        return repository.getAllByRevokedTrue();
    }

    @Override
    public int getExpiredTokensCount() {
        return getExpiredTokens().size();
    }

    @Override
    public int getRevokedTokensCount() {
        return repository.countAllByRevokedTrue();
    }

    @Transactional
    @Override
    public void deleteExpiredTokens() {
        repository.deleteAll(getExpiredTokens());
    }

    @Transactional
    @Override
    public void deleteRevokedTokens() {
        repository.deleteAllByRevokedTrue();
    }

    @Override
    public void deleteTokens(List<Token> delete) {
        repository.deleteAll(delete);
    }
}
