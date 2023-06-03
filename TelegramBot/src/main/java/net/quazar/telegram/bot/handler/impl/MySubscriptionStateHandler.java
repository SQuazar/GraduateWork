package net.quazar.telegram.bot.handler.impl;

import feign.FeignException;
import net.quazar.telegram.bot.Keyboards;
import net.quazar.telegram.bot.commands.BackToMenuCommand;
import net.quazar.telegram.bot.commands.CategoriesCommand;
import net.quazar.telegram.bot.commands.TextCommands;
import net.quazar.telegram.bot.commands.UnsubscribeCommand;
import net.quazar.telegram.bot.handler.StateHandler;
import net.quazar.telegram.proxy.ResourceServerProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@StateHandler.Handler(state = StateHandler.State.MY_SUBSCRIPTION)
public class MySubscriptionStateHandler implements StateHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(MySubscriptionStateHandler.class);

    private final ResourceServerProxy resourceServerProxy;
    private final UnsubscribeCommand unsubscribeCommand;
    private final BackToMenuCommand backToMenuCommand;

    public MySubscriptionStateHandler(ResourceServerProxy resourceServerProxy) {
        this.resourceServerProxy = resourceServerProxy;
        this.unsubscribeCommand = new UnsubscribeCommand(resourceServerProxy);
        this.backToMenuCommand = new BackToMenuCommand();
    }

    @Override
    public int handle(Update update, AbsSender absSender) throws TelegramApiException {
        Message message = update.getMessage();
        SendMessage.SendMessageBuilder messageBuilder = SendMessage.builder()
                .chatId(message.getFrom().getId());
        ResponseEntity<ResourceServerProxy.SubscriptionResponse> response;
        try {
            response = resourceServerProxy.getSubscription(message.getFrom().getId());
        } catch (FeignException.NotFound e) {
            messageBuilder.text("Подписка не найдена")
                    .replyMarkup(Keyboards.getKeyboardByState(State.START));
            absSender.execute(messageBuilder.build());
            return State.START;
        } catch (FeignException e) {
            messageBuilder.text("Что-то пошло не так.")
                    .replyMarkup(Keyboards.getKeyboardByState(State.MY_SUBSCRIPTION))
                    .build();
            absSender.execute(messageBuilder.build());
            LOGGER.warn("{} Cannot get user subscription from resource server: {}", e.status(), e.contentUTF8());
            return State.MY_SUBSCRIPTION;
        }
        var subscription = response.getBody();
        int state = State.MY_SUBSCRIPTION;
        switch (message.getText()) {
            case TextCommands.MySubscriptionCommands.CATEGORIES -> {
                return new CategoriesCommand(subscription).execute(update, absSender);
            }
            case TextCommands.MySubscriptionCommands.UNSUBSCRIBE -> {
                return unsubscribeCommand.execute(update, absSender);
            }
            case TextCommands.BACK_TO_MENU -> {
                return backToMenuCommand.execute(update, absSender);
            }
            default -> messageBuilder.replyMarkup(Keyboards.getKeyboardByState(State.MY_SUBSCRIPTION))
                    .text("Команда не найдена");
        }
        absSender.execute(messageBuilder.build());
        return state;
    }
}
