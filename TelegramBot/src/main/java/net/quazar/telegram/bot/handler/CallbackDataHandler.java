package net.quazar.telegram.bot.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.lang.annotation.*;

public interface CallbackDataHandler {
    void handle(Update update, CallbackQuery query, AbsSender absSender) throws TelegramApiException;

    final class Callbacks {
        public static final String MY_CATEGORIES = "getMyCategories";
        public static final String SUBSCRIBE_CATEGORIES = "subscribeCategories";
        public static final String SUBSCRIBE_CATEGORY = "subscribeCategory";
        public static final String UNSUBSCRIBE_CATEGORIES = "unsubscribeCategories";
        public static final String UNSUBSCRIBE_CATEGORY = "unsubscribeCategory";
        public static final String BACK_TO_MY_SUBSCRIPTION = "backToMySubscription";
        public static final String UNSUBSCRIBE = "unsubscribe";
    }

    @Component
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface Handler {
        String[] callbackData();
    }
}
