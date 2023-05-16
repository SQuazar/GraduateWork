package net.quazar.telegram.bot.handler;

import feign.FeignException;
import lombok.AllArgsConstructor;
import net.quazar.telegram.bot.Keyboards;
import net.quazar.telegram.bot.commands.TextCommands;
import net.quazar.telegram.proxy.ResourceServerProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@AllArgsConstructor
@StateHandler.Handler(state = StateHandler.State.MY_SUBSCRIPTION)
public class MySubscriptionStateHandler implements StateHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(MySubscriptionStateHandler.class);

    private final ResourceServerProxy resourceServerProxy;

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
                messageBuilder.replyMarkup(Keyboards.getKeyboardByState(state = State.CATEGORIES_EDIT));
                var categories = subscription.categories();
                if (categories == null || categories.isEmpty()) {
                    messageBuilder.text("Вы не подписаны ни на одну из категорий.");
                    break;
                }
                messageBuilder.text("Категории, на которые вы подписаны: " + String.join(", ", subscription.categories()));
            }
            case TextCommands.MySubscriptionCommands.UNSUBSCRIBE -> {
                messageBuilder.replyMarkup(Keyboards.getKeyboardByState(state = State.START));
                try {
                    resourceServerProxy.unsubscribe(message.getFrom().getId());
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
                messageBuilder.text("Вы отписались от новостной рассылки");
            }
            case TextCommands.BACK_TO_MENU -> {
                messageBuilder.replyMarkup(Keyboards.getKeyboardByState(state = State.MENU));
                messageBuilder.text("Вы вернулись в главное меню");
            }
            default -> messageBuilder.replyMarkup(Keyboards.getKeyboardByState(State.MY_SUBSCRIPTION))
                    .text("Команда не найдена");
        }
        absSender.execute(messageBuilder.build());
        return state;
    }
}
