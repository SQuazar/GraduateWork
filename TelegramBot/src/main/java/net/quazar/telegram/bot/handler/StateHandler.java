package net.quazar.telegram.bot.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.lang.annotation.*;

/**
 * Интерфейс обработки состояния
 */
public interface StateHandler {
    /**
     * Метод обработки состояния
     * @param update полученное обновление от телеграма
     * @param absSender экземпляр бота
     * @return новое состояние
     */
    int handle(Update update, AbsSender absSender) throws TelegramApiException;

    class State {
        public static final int START = 0;
        public static final int MENU = 1;
    }

    @Component
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface Handler {
        int state();
    }
}
