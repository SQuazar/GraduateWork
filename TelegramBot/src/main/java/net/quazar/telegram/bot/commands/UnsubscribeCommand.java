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
public class UnsubscribeCommand implements BotCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(UnsubscribeCommand.class);

    private final ResourceServerProxy resourceServerProxy;

    @Override
    public int execute(Update update, AbsSender absSender) throws TelegramApiException {
        Message message = update.getMessage();
        int state;
        SendMessage.SendMessageBuilder messageBuilder = SendMessage.builder()
                        .chatId(update.getMessage().getFrom().getId());
        messageBuilder.replyMarkup(Keyboards.getKeyboardByState(state = StateHandler.State.START));
        try {
            resourceServerProxy.unsubscribe(message.getFrom().getId());
        } catch (FeignException.NotFound e) {
            messageBuilder.text("Подписка не найдена")
                    .replyMarkup(Keyboards.getKeyboardByState(StateHandler.State.START));
            absSender.execute(messageBuilder.build());
            return StateHandler.State.START;
        } catch (FeignException e) {
            messageBuilder.text("Что-то пошло не так.")
                    .replyMarkup(Keyboards.getKeyboardByState(StateHandler.State.MY_SUBSCRIPTION))
                    .build();
            absSender.execute(messageBuilder.build());
            LOGGER.warn("{} Cannot get user subscription from resource server: {}", e.status(), e.contentUTF8());
            return StateHandler.State.MY_SUBSCRIPTION;
        }
        messageBuilder.text("Вы отписались от новостной рассылки");
        absSender.execute(messageBuilder.build());
        return state;
    }
}
