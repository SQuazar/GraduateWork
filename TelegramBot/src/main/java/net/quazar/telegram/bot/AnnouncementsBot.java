package net.quazar.telegram.bot;

import feign.FeignException;
import net.quazar.telegram.bot.handler.impl.AnnouncementBotStateHandlerRegistry;
import net.quazar.telegram.bot.handler.StateHandler;
import net.quazar.telegram.bot.handler.StateHandlerRegistry;
import net.quazar.telegram.proxy.ResourceServerProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

@Component
public class AnnouncementsBot extends TelegramLongPollingBot {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnnouncementsBot.class);

    @Value("${bot.username}")
    private String username;
    private final StateHandlerRegistry stateHandlerRegistry;
    private final ResourceServerProxy resourceServerProxy;

    public AnnouncementsBot(@Value("${bot.token}") String token,
                            ResourceServerProxy resourceServerProxy) {
        super(token);
        this.stateHandlerRegistry = new AnnouncementBotStateHandlerRegistry();
        this.resourceServerProxy = resourceServerProxy;
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage()) return;
        if (!update.getMessage().hasText()) return;
        int state;
        try {
            state = resourceServerProxy.getSubscription(update.getMessage().getFrom().getId()).getBody().state();
        } catch (FeignException e) {
            state = 0;
        }
        if (update.getMessage().getText().equals("/start") && state == 0) {
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(update.getMessage().getChatId())
                    .text("Для подписки на новостную рассылку нажмите кнопку \"Подписаться\"")
                    .replyMarkup(Keyboards.getKeyboardByState(StateHandler.State.START))
                    .build();
            try {
                sendApiMethod(sendMessage);
            } catch (TelegramApiException e) {
                LOGGER.warn("Cannot execute api method", e);
            }
            return;
        }
        var handlers = stateHandlerRegistry.getStateHandlers(state);
        if (handlers == null) {
            SendMessage message = SendMessage.builder()
                    .chatId(update.getMessage().getFrom().getId())
                    .text("Неверный ввод. Возвращаем в главное меню.")
                    .replyMarkup(Keyboards.startKeyboard())
                    .build();
            try {
                sendApiMethod(message);
            } catch (TelegramApiException e) {
                LOGGER.warn("Cannot execute api method", e);
            }
            return;
        }
        handlers.forEach(handler -> {
            int nextState = 0;
            try {
                nextState = handler.handle(update, this);
            } catch (TelegramApiRequestException e) {
                LOGGER.warn("Cannot execute api method", e);
                if (e.getApiResponse().contains("Can't access the chat") || e.getApiResponse().contains("Bot was blocked by the user")) {
                    try {
                        resourceServerProxy.unsubscribe(update.getMessage().getFrom().getId());
                    } catch (FeignException exception) {
                        LOGGER.warn("{} Something went is wrong: {}", exception.status(), exception.contentUTF8());
                    }
                }
            } catch (TelegramApiException e) {
                LOGGER.warn("Cannot execute api method", e);
            }
            if (nextState == 0) return;
            try {
                resourceServerProxy.changeState(update.getMessage().getFrom().getId(), nextState);
            } catch (FeignException.NotFound e) {
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(update.getMessage().getFrom().getId())
                        .text("Подписка не найдена")
                        .replyMarkup(Keyboards.getKeyboardByState(StateHandler.State.START))
                        .build();
                try {
                    sendApiMethod(sendMessage);
                } catch (TelegramApiException ex) {
                    LOGGER.warn("Cannot execute api method", e);
                }
            } catch (FeignException e) {
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(update.getMessage().getFrom().getId())
                        .text("Что-то пошло не так.")
                        .replyMarkup(Keyboards.getKeyboardByState(StateHandler.State.START))
                        .build();
                LOGGER.warn("{} Не удалось получить подписку пользователя: {}", e.status(), e.contentUTF8());
                try {
                    sendApiMethod(sendMessage);
                } catch (TelegramApiException ex) {
                    LOGGER.warn("Cannot execute api method", e);
                }
            }
        });
    }

    public StateHandlerRegistry getStateHandlerRegistry() {
        return stateHandlerRegistry;
    }

    private static BotState state = BotState.AVAILABLE;

    public static BotState getBotState() {
        return state;
    }

    public static void setBotState(@NonNull BotState state) {
        AnnouncementsBot.state = state;
    }

    public enum BotState {
        AVAILABLE,
        SENDING_ANNOUNCEMENTS
    }
}
