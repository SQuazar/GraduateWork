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

import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@StateHandler.Handler(state = StateHandler.State.MENU)
public class MenuStateHandler implements StateHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(MenuStateHandler.class);

    private final ResourceServerProxy resourceServerProxy;
    private final DateTimeFormatter dateTimeFormatter;

    @Override
    public int handle(Update update, AbsSender absSender) throws TelegramApiException {
        Message message = update.getMessage();
        if (message.getText().equals(TextCommands.MenuCommands.MY_SUBSCRIPTION)) {
            ResponseEntity<ResourceServerProxy.SubscriptionResponse> response;
            try {
                response = resourceServerProxy.getSubscription(message.getFrom().getId());
            } catch (FeignException.NotFound e) {
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(message.getFrom().getId())
                        .text("Подписка не найдена")
                        .replyMarkup(Keyboards.getKeyboardByState(State.START))
                        .build();
                absSender.execute(sendMessage);
                return State.START;
            } catch (FeignException e) {
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(message.getFrom().getId())
                        .text("Что-то пошло не так.")
                        .replyMarkup(Keyboards.getKeyboardByState(State.START))
                        .build();
                absSender.execute(sendMessage);
                LOGGER.warn("{} Не удалось получить подписку пользователя: {}",e.status(), e.contentUTF8());
                return State.MENU;
            }
            var subscription = response.getBody();
            StringBuilder subscriptionInfo = new StringBuilder("Ваша подписка: \n");
            subscriptionInfo.append("Ваш ID: ").append(subscription.userId()).append('\n')
                    .append("Подписан с: ")
                    .append(subscription.subscriptionDate().format(dateTimeFormatter)).append('\n');
            if (subscription.roles() != null && !subscription.roles().isEmpty())
                subscriptionInfo.append("Ваши роли: ").append(String.join(", ", subscription.roles()));
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(message.getFrom().getId())
                    .text(subscriptionInfo.toString())
                    .replyMarkup(Keyboards.getKeyboardByState(State.MY_SUBSCRIPTION))
                    .build();
            absSender.execute(sendMessage);
            return State.MY_SUBSCRIPTION;
        }
        SendMessage sendMessage = SendMessage.builder()
                .chatId(message.getFrom().getId())
                .text("Команда не найдена")
                .replyMarkup(Keyboards.getKeyboardByState(State.MENU))
                .build();
        absSender.execute(sendMessage);
        return State.MENU;
    }
}
