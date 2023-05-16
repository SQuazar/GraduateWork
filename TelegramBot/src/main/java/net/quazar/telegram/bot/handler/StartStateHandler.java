package net.quazar.telegram.bot.handler;

import feign.FeignException;
import lombok.AllArgsConstructor;
import net.quazar.telegram.bot.Keyboards;
import net.quazar.telegram.bot.commands.TextCommands;
import net.quazar.telegram.proxy.ResourceServerProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@AllArgsConstructor
@StateHandler.Handler(state = StateHandler.State.START)
public class StartStateHandler implements StateHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(StartStateHandler.class);

    private final ResourceServerProxy resourceServerProxy;

    @Override
    public int handle(Update update, AbsSender absSender) throws TelegramApiException {
        Message message = update.getMessage();
        if (!message.getText().equals(TextCommands.StartCommands.SUBSCRIBE)) {
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(message.getFrom().getId())
                    .text("Неверная команда")
                    .replyMarkup(Keyboards.getKeyboardByState(State.START))
                    .build();
            absSender.execute(sendMessage);
            return State.START;
        }
        try {
            resourceServerProxy.getSubscription(message.getFrom().getId());
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(message.getFrom().getId())
                    .text("Вы уже подписаны на новостную рассылку")
                    .replyMarkup(Keyboards.getKeyboardByState(State.MENU))
                    .build();
            absSender.execute(sendMessage);
            return State.MENU;
        } catch (FeignException.NotFound e) {
            try {
                resourceServerProxy.subscribe(message.getFrom().getId());
            } catch (FeignException exception) {
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(message.getFrom().getId())
                        .text("Что-то пошло не так.")
                        .replyMarkup(Keyboards.getKeyboardByState(State.START))
                        .build();
                absSender.execute(sendMessage);
                LOGGER.warn("{} Не удалось оформить новостную подписку: {}", exception.status(), exception.contentUTF8());
                return State.START;
            }
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(message.getFrom().getId())
                    .text("Вы успешно подписались на новостную рассылку")
                    .replyMarkup(Keyboards.getKeyboardByState(State.MENU))
                    .build();
            absSender.execute(sendMessage);
            return State.MENU;
        }
    }
}
