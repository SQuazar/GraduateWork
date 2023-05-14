package net.quazar.resourceserver.controller;

import net.quazar.resourceserver.entity.TelegramUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class TestRest {
    @GetMapping
    public TelegramUser user() {
        return TelegramUser.builder()
                .telegramId(1111111L)
                .subscriptionDate(LocalDateTime.now())
                .permissions(List.of("12", "3"))
                .build();
    }
}
