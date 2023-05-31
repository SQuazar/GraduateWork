package net.quazar.authorizationserver.service;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@Service
public class TokenCleanupService {
    private final TokenService tokenService;

//    @Scheduled(cron = "0 0 0 * * *", zone = "Europe/Moscow")
    @Scheduled(fixedDelay = 60, initialDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void cleanTokens() {
        tokenService.deleteRevokedTokens();
        tokenService.deleteExpiredTokens();
    }
}
