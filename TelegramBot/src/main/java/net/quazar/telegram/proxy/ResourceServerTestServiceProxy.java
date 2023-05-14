package net.quazar.telegram.proxy;

import net.quazar.telegram.entity.dto.TelegramUserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "resource-server")
public interface ResourceServerTestServiceProxy {
    @GetMapping("/api/users")
    TelegramUserDto getUser();
}
