package net.quazar.telegram.bot.commands;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface BotCommand {
    /**
     * Выполняет команду бота
     * @param update обновление Telegram
     * @param chatId id чата
     * @param absSender бот
     * @return новое состояние
     */
    int execute(Update update, long chatId, AbsSender absSender) throws TelegramApiException;
}
