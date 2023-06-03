package net.quazar.telegram.bot.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.lang.annotation.*;

public interface CallbackDataHandler {
    void handle(Update update, CallbackQuery query, AbsSender sender);

    @Component
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface Handler {
        String[] callbackData();
    }
}
