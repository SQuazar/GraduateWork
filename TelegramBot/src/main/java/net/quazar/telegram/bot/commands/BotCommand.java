package net.quazar.telegram.bot.commands;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface BotCommand {
    /**
     * Выполняет команду бота
     * @param update обновление Telegram
     * @return новое состояние
     */
    int execute(Update update, AbsSender absSender) throws TelegramApiException;
}
