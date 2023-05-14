package net.quazar.apigateway.controller;

import lombok.AllArgsConstructor;
import net.quazar.apigateway.entity.TelegramUserDto;
import net.quazar.apigateway.proxy.ResourceServerProxy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/resource/user")
@AllArgsConstructor
public class ResourceServerController {

    private final ResourceServerProxy proxy;

    @PreAuthorize("hasAuthority('sa2')")
    @GetMapping
    public TelegramUserDto getUser() {
        return proxy.getUser();
    }
}
