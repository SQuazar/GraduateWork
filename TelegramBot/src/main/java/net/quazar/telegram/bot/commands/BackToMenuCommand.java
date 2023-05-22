package net.quazar.telegram.bot.commands;

import net.quazar.telegram.bot.Keyboards;
import net.quazar.telegram.bot.handler.StateHandler;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class BackToMenuCommand implements BotCommand {
    @Override
    public int execute(Update update, AbsSender absSender) throws TelegramApiException {
        SendMessage.SendMessageBuilder messageBuilder = SendMessage.builder()
                .chatId(update.getMessage().getFrom().getId());
        int state;
        messageBuilder.replyMarkup(Keyboards.getKeyboardByState(state = StateHandler.State.MENU));
        messageBuilder.text("Вы вернулись в главное меню");
        absSender.execute(messageBuilder.build());
        return state;
    }
}
