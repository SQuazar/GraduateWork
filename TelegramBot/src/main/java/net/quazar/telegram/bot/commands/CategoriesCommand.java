package net.quazar.telegram.bot.commands;

import lombok.AllArgsConstructor;
import net.quazar.telegram.bot.handler.CallbackDataHandler;
import net.quazar.telegram.bot.handler.StateHandler;
import net.quazar.telegram.proxy.ResourceServerProxy;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@AllArgsConstructor
public class CategoriesCommand implements BotCommand {
    private final ResourceServerProxy.SubscriptionResponse subscription;

    @Override
    public int execute(Update update, long chatId, AbsSender absSender) throws TelegramApiException {
        SendMessage.SendMessageBuilder messageBuilder = SendMessage.builder()
                .chatId(chatId);

        messageBuilder.replyMarkup(InlineKeyboardMarkup.builder()
                .keyboard(List.of(
                        List.of(
                                InlineKeyboardButton.builder()
                                        .text("Подписаться")
                                        .callbackData(CallbackDataHandler.Callbacks.SUBSCRIBE_CATEGORIES)
                                        .build(),
                                InlineKeyboardButton.builder()
                                        .text("Отписаться")
                                        .callbackData(CallbackDataHandler.Callbacks.UNSUBSCRIBE_CATEGORIES)
                                        .build()
                        ),
                        List.of(InlineKeyboardButton.builder()
                                .text("Вернуться в мою подписку")
                                .callbackData(CallbackDataHandler.Callbacks.BACK_TO_MY_SUBSCRIPTION)
                                .build())
                ))
                .build());
        var categories = subscription.categories();
        if (categories == null || categories.isEmpty()) {
            messageBuilder.text("Вы не подписаны ни на одну из категорий.");
            absSender.execute(messageBuilder.build());
            return StateHandler.State.MENU;
        }
        messageBuilder.text("Категории, на которые вы подписаны: " + String.join(", ", subscription.categories()));
        absSender.execute(messageBuilder.build());
        return StateHandler.State.MENU;
    }
}
