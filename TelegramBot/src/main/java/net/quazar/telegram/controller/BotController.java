package net.quazar.telegram.controller;

import lombok.AllArgsConstructor;
import net.quazar.telegram.entity.dto.TelegramUserDto;
import net.quazar.telegram.proxy.ResourceServerTestServiceProxy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/bot/users")
public class BotController {

    private final ResourceServerTestServiceProxy proxy;

    @GetMapping
    public TelegramUserDto getTelegramUser() {
        return proxy.getUser();
    }

    @GetMapping("/test")
    public String test() {
        return "1";
    }
}
