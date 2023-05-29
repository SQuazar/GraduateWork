package net.quazar.apigateway.controller;

import lombok.AllArgsConstructor;
import net.quazar.apigateway.proxy.ResourceServerProxy;
import net.quazar.apigateway.proxy.TelegramBotServerProxy;
import net.quazar.apigateway.proxy.resource.CategoryResponse;
import net.quazar.apigateway.proxy.resource.RoleResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/telegram")
public class TelegramBotController {
    private final TelegramBotServerProxy telegramBotServerProxy;
    private final ResourceServerProxy resourceServerProxy;

    @PreAuthorize("hasAuthority('telegram.announcements.send')")
    @PostMapping("/sendAnnouncements")
    public void startSendingAnnouncements(@RequestBody TelegramBotServerProxy.SendAnnouncementsRequest request, Authentication authentication) {
        var user = resourceServerProxy.getUserByUsername(authentication.getName());
        var category = resourceServerProxy.getCategories(List.of(request.getCategoryId()))
                .stream()
                .map(CategoryResponse::name)
                .toList();
        var roles = resourceServerProxy.getAllRolesBy(request.getRolesIds().stream().toList())
                .stream()
                .map(RoleResponse::name)
                .toList();
        resourceServerProxy.saveAnnouncement(request.getText(), user.id(), category, roles);
        telegramBotServerProxy.startSendingAnnouncements(request);
    }

    @PreAuthorize("hasAuthority('telegram.announcements.send')")
    @GetMapping("/state")
    public TelegramBotServerProxy.BotStateResponse getState() {
        return telegramBotServerProxy.getState();
    }
}
