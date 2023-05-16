package net.quazar.telegram.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.quazar.telegram.bot.AnnouncementsBot;
import net.quazar.telegram.exception.BotNotAvailableException;
import net.quazar.telegram.service.AnnouncementsDeliveryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@RestController
public class TelegramBotController {
    private final AnnouncementsDeliveryService deliveryService;

    @PostMapping("/sendAnnouncements")
    public void startSendingAnnouncements(@RequestBody SendAnnouncementsRequest request) {
        if (AnnouncementsBot.getBotState() == AnnouncementsBot.BotState.SENDING_ANNOUNCEMENTS)
            throw new BotNotAvailableException("В данный момент бот занят рассылкой новостей.");
        if (request.getText() == null || request.getText().isEmpty()) return;
        deliveryService.startSendingAnnouncement(request.text, request.categoryId, request.rolesIds);
    }

    @Data
    public static final class SendAnnouncementsRequest {
        private final String text;
        @JsonProperty(value = "category_id", defaultValue = "0")
        private final int categoryId;
        @JsonProperty("roles")
        private final Set<Integer> rolesIds = new HashSet<>();
    }
}
