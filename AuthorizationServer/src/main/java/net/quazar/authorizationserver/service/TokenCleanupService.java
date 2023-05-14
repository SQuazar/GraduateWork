package net.quazar.authorizationserver.service;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class TokenCleanupService {
    private final TokenService tokenService;

    @Scheduled(cron = "0 0 0 * * *", zone = "Europe/Moscow")
    public void cleanTokens() {
        tokenService.deleteRevokedTokens();
        tokenService.deleteExpiredTokens();
    }
}
