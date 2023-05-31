package net.quazar.authorizationserver.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import net.quazar.authorizationserver.config.service.JwtService;
import net.quazar.authorizationserver.controller.entity.AuthorizationResponse;
import net.quazar.authorizationserver.controller.entity.TokenResponse;
import net.quazar.authorizationserver.controller.entity.VerifyTokenResponse;
import net.quazar.authorizationserver.entity.Token;
import net.quazar.authorizationserver.entity.User;
import net.quazar.authorizationserver.exception.InvalidTokenException;
import net.quazar.authorizationserver.exception.TokenExpireException;
import net.quazar.authorizationserver.exception.TokenRevokeException;
import net.quazar.authorizationserver.service.AuthenticationService;
import net.quazar.authorizationserver.service.TokenService;
import net.quazar.authorizationserver.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenService tokenService;

    @Transactional
    @Override
    public AuthorizationResponse login(@NonNull String username, @NonNull String password) {
        var auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        User user = (User) auth.getPrincipal();
        var refreshTokens = tokenService.getByUser(user).stream()
                .filter(token -> token.getType() == Token.TokenType.REFRESH)
                .toList();
        if (refreshTokens.size() >= 50) {
            List<Token> delete = refreshTokens.stream().sorted(Comparator.comparingLong(Token::getExpire))
                    .limit(10)
                    .toList();
            tokenService.deleteTokens(delete);
        }
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        var access = tokenService.saveToken(accessToken, user);
        var refresh = tokenService.saveToken(refreshToken, Token.TokenType.REFRESH, user);
        return AuthorizationResponse.builder()
                .message("Успешный вход")
                .accessToken(access.getToken())
                .refreshToken(refresh.getToken())
                .build();
    }

    @Transactional
    @Override
    public TokenResponse refreshToken(@NonNull String refreshToken) {
        if (StringUtils.isEmpty(refreshToken))
            throw new InvalidTokenException("Неверный токен");
        Token tokenEntity = tokenService.getByToken(refreshToken);
        if (tokenEntity.getType() != Token.TokenType.REFRESH)
            throw new InvalidTokenException("Неверный токен");
        if (tokenEntity.isRevoked())
            throw new TokenRevokeException("Данный токен был отозван");
        if (jwtService.isTokenExpired(refreshToken))
            throw new TokenExpireException("Срок действия токена истёк");
        String username = jwtService.extractUsername(refreshToken);
        if (username == null)
            throw new UsernameNotFoundException("Пользователь не найден");
        User user = userService.getByUsername(username);
        if (!jwtService.isTokenValid(refreshToken, user))
            throw new InvalidTokenException("Неверный токен");
        String accessToken = jwtService.generateToken(user);
        tokenService.deleteToken(refreshToken);
        refreshToken = jwtService.generateRefreshToken(user);
        var access = tokenService.saveToken(accessToken, user);
        var refresh = tokenService.saveToken(refreshToken, Token.TokenType.REFRESH, user);
        return TokenResponse.builder()
                .accessToken(access.getToken())
                .refreshToken(refresh.getToken())
                .build();
    }

    @Override
    public VerifyTokenResponse verifyAccessToken(@NonNull String accessToken) {
        Token tokenEntity = tokenService.getByToken(accessToken);
        if (tokenEntity.isRevoked())
            throw new TokenRevokeException("Данный токен был отозван");
        if (jwtService.isTokenExpired(accessToken))
            throw new TokenExpireException("Срок действия токена истёк");
        String username = jwtService.extractUsername(accessToken);
        if (username == null)
            throw new UsernameNotFoundException("Пользователь не найден");
        User user = userService.getByUsername(username);
        if (!jwtService.isTokenValid(accessToken, user))
            throw new InvalidTokenException("Неверный токен");
        return VerifyTokenResponse.builder()
                .message("Отличный токен!")
                .build();
    }
}
