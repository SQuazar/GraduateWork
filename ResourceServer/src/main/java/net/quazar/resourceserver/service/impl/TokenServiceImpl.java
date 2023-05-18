package net.quazar.resourceserver.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import net.quazar.resourceserver.entity.Token;
import net.quazar.resourceserver.entity.User;
import net.quazar.resourceserver.entity.dto.TokenDto;
import net.quazar.resourceserver.exception.TokenNotFoundException;
import net.quazar.resourceserver.exception.UserNotFoundException;
import net.quazar.resourceserver.mapper.TokenDtoMapper;
import net.quazar.resourceserver.repository.TokenRepository;
import net.quazar.resourceserver.repository.UserRepository;
import net.quazar.resourceserver.service.TokenService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {
    private final TokenRepository repository;
    private final UserRepository userRepository;
    private final TokenDtoMapper tokenDtoMapper = TokenDtoMapper.INSTANCE;

    @Override
    public TokenDto saveToken(@NonNull String token, int userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("Пользователь с ID %d не найден".formatted(userId)));
        return tokenDtoMapper.tokenToDto(repository.save(Token.builder()
                .token(token)
                .user(user)
                .build()));
    }

    @Override
    public TokenDto saveToken(@NonNull String token, @NonNull Token.TokenType type, int userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("Пользователь с ID %d не найден".formatted(userId)));
        return tokenDtoMapper.tokenToDto(repository.save(Token.builder()
                .token(token)
                .type(type)
                .user(user)
                .build()));
    }

    @Override
    public TokenDto getByToken(@NonNull String token) throws TokenNotFoundException {
        return repository.findByToken(token)
                .map(tokenDtoMapper::tokenToDto)
                .orElseThrow(() -> new TokenNotFoundException("Токен не найден"));
    }

    @Override
    public void deleteToken(@NonNull String token) {
        token = getByToken(token).getToken();
        repository.deleteByToken(token);
    }

    @Override
    public List<TokenDto> getByUserId(int userId) {
        return repository.findAllByUserId(userId)
                .stream()
                .map(tokenDtoMapper::tokenToDto)
                .toList();
    }

    @Override
    public List<TokenDto> getExpiredTokens() {
        return repository.findAll().stream()
                .filter(token -> token.getExpire() < System.currentTimeMillis())
                .map(tokenDtoMapper::tokenToDto)
                .toList();
    }

    @Override
    public List<TokenDto> getRevokedTokens() {
        return repository.getAllByRevokedTrue()
                .stream()
                .map(tokenDtoMapper::tokenToDto)
                .toList();
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
        repository.deleteAllById(getExpiredTokens()
                .stream()
                .map(TokenDto::getId)
                .toList());
    }

    @Transactional
    @Override
    public void deleteRevokedTokens() {
        repository.deleteAllByRevokedTrue();
    }

    @Override
    public void deleteTokens(List<String> tokens) {
        repository.deleteAllByToken(tokens);
    }
}
