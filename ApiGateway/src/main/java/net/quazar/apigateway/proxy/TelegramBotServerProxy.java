package net.quazar.apigateway.proxy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashSet;
import java.util.Set;

@FeignClient("telegram-bot")
public interface TelegramBotServerProxy {
    @PostMapping("/sendAnnouncements")
    void startSendingAnnouncements(@RequestBody SendAnnouncementsRequest request);

    @GetMapping("/state")
    BotStateResponse getState();

    @Data
    final class SendAnnouncementsRequest {
        private final String text;
        @JsonProperty(value = "category_id", defaultValue = "0")
        private final int categoryId;
        @JsonProperty("roles")
        private final Set<Integer> rolesIds = new HashSet<>();
    }

    record BotStateResponse(int code, String message) {}
}
