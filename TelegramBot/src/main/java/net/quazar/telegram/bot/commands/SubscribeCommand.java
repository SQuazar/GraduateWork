package net.quazar.telegram.bot.commands;

import feign.FeignException;
import lombok.AllArgsConstructor;
import net.quazar.telegram.bot.Keyboards;
import net.quazar.telegram.bot.handler.StateHandler;
import net.quazar.telegram.proxy.ResourceServerProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@AllArgsConstructor
public class SubscribeCommand implements BotCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubscribeCommand.class);

    private final ResourceServerProxy resourceServerProxy;

    @Override
    public int execute(Update update, AbsSender absSender) throws TelegramApiException {
        Message message = update.getMessage();
        try {
            resourceServerProxy.getSubscription(message.getFrom().getId());
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(message.getFrom().getId())
                    .text("Вы уже подписаны на новостную рассылку")
                    .replyMarkup(Keyboards.getKeyboardByState(StateHandler.State.MENU))
                    .build();
            absSender.execute(sendMessage);
            return StateHandler.State.MENU;
        } catch (FeignException.NotFound e) {
            try {
                resourceServerProxy.subscribe(message.getFrom().getId());
            } catch (FeignException exception) {
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(message.getFrom().getId())
                        .text("Что-то пошло не так.")
                        .replyMarkup(Keyboards.getKeyboardByState(StateHandler.State.START))
                        .build();
                absSender.execute(sendMessage);
                LOGGER.warn("{} Не удалось оформить новостную подписку: {}", exception.status(), exception.contentUTF8());
                return StateHandler.State.START;
            }
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(message.getFrom().getId())
                    .text("Вы успешно подписались на новостную рассылку")
                    .replyMarkup(Keyboards.getKeyboardByState(StateHandler.State.MENU))
                    .build();
            absSender.execute(sendMessage);
            return StateHandler.State.MENU;
        }
    }
}
