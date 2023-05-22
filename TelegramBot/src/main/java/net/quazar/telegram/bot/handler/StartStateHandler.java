package net.quazar.telegram.bot.handler;

import net.quazar.telegram.bot.Keyboards;
import net.quazar.telegram.bot.commands.SubscribeCommand;
import net.quazar.telegram.bot.commands.TextCommands;
import net.quazar.telegram.proxy.ResourceServerProxy;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@StateHandler.Handler(state = StateHandler.State.START)
public class StartStateHandler implements StateHandler {
    private final SubscribeCommand subscribeCommand;

    public StartStateHandler(ResourceServerProxy resourceServerProxy) {
        this.subscribeCommand = new SubscribeCommand(resourceServerProxy);
    }

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
        return subscribeCommand.execute(update, absSender);
    }
}
