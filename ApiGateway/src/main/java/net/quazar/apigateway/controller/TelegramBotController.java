package net.quazar.apigateway.controller;

import lombok.AllArgsConstructor;
import net.quazar.apigateway.proxy.ResourceServerProxy;
import net.quazar.apigateway.proxy.TelegramBotServerProxy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/telegram")
public class TelegramBotController {
    private final TelegramBotServerProxy telegramBotServerProxy;
    private final ResourceServerProxy resourceServerProxy;

    @PreAuthorize("hasAuthority('telegram.announcements.send')")
    @PostMapping("/sendAnnouncements")
    public void startSendingAnnouncements(@RequestBody TelegramBotServerProxy.SendAnnouncementsRequest request) {
        resourceServerProxy.saveAnnouncement(request.getText());
        telegramBotServerProxy.startSendingAnnouncements(request);
    }
}
