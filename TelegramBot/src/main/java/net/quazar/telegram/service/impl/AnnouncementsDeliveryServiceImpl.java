package net.quazar.telegram.service.impl;

import feign.FeignException;
import lombok.AllArgsConstructor;
import net.quazar.telegram.bot.AnnouncementsBot;
import net.quazar.telegram.exception.BotNotAvailableException;
import net.quazar.telegram.proxy.ResourceServerProxy;
import net.quazar.telegram.service.AnnouncementsDeliveryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
@Service
public class AnnouncementsDeliveryServiceImpl implements AnnouncementsDeliveryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnnouncementsDeliveryService.class);

    private final ResourceServerProxy resourceServerProxy;
    private final AnnouncementsBot announcementsBot;

    @Override
    public void startSendingAnnouncement(String text, int category, Set<Integer> roles) {
        Set<Long> users = new HashSet<>();
        try {
            if (category != 0) {
                users.addAll(Objects.requireNonNull(resourceServerProxy.usersIdsByCategory(category).getBody()));
            } else if (roles.isEmpty())
                users.addAll(Objects.requireNonNull(resourceServerProxy.getAllUsers().getBody()));
            if (!roles.isEmpty()) {
                var byRole = roles
                        .stream()
                        .map(role -> resourceServerProxy.usersIdsByRole(role).getBody()).filter(Objects::nonNull)
                        .flatMap(Collection::stream)
                        .toList();
                users.removeIf(id -> !byRole.contains(id));
                users.addAll(byRole);
            }
        } catch (FeignException e) {
            LOGGER.warn("Cannot get data from resource server", e);
            throw new BotNotAvailableException("Неудалось получить информацию с сервера ресурсов", e);
        }
        LOGGER.info("Receivers: {}", users);
        AnnouncementsBot.setBotState(AnnouncementsBot.BotState.SENDING_ANNOUNCEMENTS);
        CompletableFuture.runAsync(() -> {
            for (long userId : users) {
                synchronized (Thread.currentThread()) {
                    try {
                        Thread.currentThread().wait(35);
                    } catch (InterruptedException e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                }
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(userId)
                        .text(text)
                        .disableWebPagePreview(false)
                        .build();
                try {
                    announcementsBot.execute(sendMessage);
                } catch (TelegramApiRequestException e) {
                    if (e.getApiResponse().contains("can't access the chat") || e.getApiResponse().contains("bot was blocked by the user")) {
                        try {
                            resourceServerProxy.unsubscribe(userId);
                            LOGGER.info("User {} is now unsubscribed because chat is blocked or not accessed", userId);
                        } catch (FeignException exception) {
                            LOGGER.warn("{} Something went is wrong: {}", exception.status(), exception.contentUTF8());
                        }
                    } else {
                        LOGGER.warn("Cannot execute api method", e);
                    }
                } catch (TelegramApiException e) {
                    LOGGER.warn("Cannot execute api method", e);
                }
            }
        }).whenComplete((v, t) -> AnnouncementsBot.setBotState(AnnouncementsBot.BotState.AVAILABLE));
    }
}
