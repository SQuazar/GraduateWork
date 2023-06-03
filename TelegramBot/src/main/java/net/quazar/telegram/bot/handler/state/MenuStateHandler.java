package net.quazar.telegram.bot.handler.state;

import net.quazar.telegram.bot.Keyboards;
import net.quazar.telegram.bot.commands.MySubscriptionCommand;
import net.quazar.telegram.bot.commands.TextCommands;
import net.quazar.telegram.bot.handler.StateHandler;
import net.quazar.telegram.proxy.ResourceServerProxy;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.format.DateTimeFormatter;

@StateHandler.Handler(state = StateHandler.State.MENU)
public class MenuStateHandler implements StateHandler {
    private final MySubscriptionCommand mySubscriptionCommand;

    public MenuStateHandler(ResourceServerProxy resourceServerProxy, DateTimeFormatter dateTimeFormatter) {
        this.mySubscriptionCommand = new MySubscriptionCommand(resourceServerProxy, dateTimeFormatter);
    }

    @Override
    public int handle(Update update, AbsSender absSender) throws TelegramApiException {
        Message message = update.getMessage();
        if (message.getText().equals(TextCommands.MenuCommands.MY_SUBSCRIPTION))
            return mySubscriptionCommand.execute(update, absSender);
        SendMessage sendMessage = SendMessage.builder()
                .chatId(message.getFrom().getId())
                .text("Команда не найдена")
                .replyMarkup(Keyboards.getKeyboardByState(State.MENU))
                .build();
        absSender.execute(sendMessage);
        return State.MENU;
    }
}
