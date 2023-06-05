package net.quazar.telegram.bot.commands;

import lombok.AllArgsConstructor;
import net.quazar.telegram.bot.handler.CallbackDataHandler;
import net.quazar.telegram.bot.handler.StateHandler;
import net.quazar.telegram.bot.service.SubscriptionService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.format.DateTimeFormatter;
import java.util.List;

@AllArgsConstructor
public class MySubscriptionCommand implements BotCommand {

    private final SubscriptionService subscriptionService;
    private final DateTimeFormatter dateTimeFormatter;

    @Override
    public int execute(Update update, long chatId, AbsSender absSender) throws TelegramApiException {
        var subscription = subscriptionService.getSubscription(chatId);
        StringBuilder subscriptionInfo = new StringBuilder("Ваша подписка: \n");
        subscriptionInfo.append("Ваш ID: ").append(subscription.userId()).append('\n')
                .append("Подписан с: ")
                .append(subscription.subscriptionDate().format(dateTimeFormatter)).append('\n');
        if (subscription.roles() != null && !subscription.roles().isEmpty())
            subscriptionInfo.append("Ваши роли: ").append(String.join(", ", subscription.roles()));
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(subscriptionInfo.toString())
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboard(List.of(
                                List.of(InlineKeyboardButton.builder()
                                        .text("Мои категории")
                                        .callbackData(CallbackDataHandler.Callbacks.MY_CATEGORIES)
                                        .build()),
                                List.of(InlineKeyboardButton.builder()
                                        .text("Отменить подписку")
                                        .callbackData(CallbackDataHandler.Callbacks.UNSUBSCRIBE)
                                        .build())
                        ))
                        .build())
                .build();
        absSender.execute(sendMessage);
        return StateHandler.State.MENU;
    }
}
