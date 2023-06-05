package net.quazar.telegram.bot.handler.callback;

import lombok.AllArgsConstructor;
import net.quazar.telegram.bot.commands.MySubscriptionCommand;
import net.quazar.telegram.bot.handler.CallbackDataHandler;
import net.quazar.telegram.bot.service.SubscriptionService;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@CallbackDataHandler.Handler(callbackData = CallbackDataHandler.Callbacks.BACK_TO_MY_SUBSCRIPTION)
public class BackToSubscriptionButton implements CallbackDataHandler {
    private final SubscriptionService subscriptionService;
    private final DateTimeFormatter dateTimeFormatter;

    @Override
    public void handle(Update update, CallbackQuery query, AbsSender absSender) throws TelegramApiException {
        DeleteMessage deleteMessage = DeleteMessage.builder()
                .messageId(query.getMessage().getMessageId())
                .chatId(query.getMessage().getChatId())
                .build();
        absSender.execute(deleteMessage);

        new MySubscriptionCommand(subscriptionService, dateTimeFormatter).execute(update, query.getFrom().getId(), absSender);
    }
}
