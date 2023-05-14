package net.quazar.apigateway.proxy;

import net.quazar.apigateway.entity.TelegramUserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "resource-server")
public interface ResourceServerProxy {
    @GetMapping("/api/users")
    TelegramUserDto getUser();
}
