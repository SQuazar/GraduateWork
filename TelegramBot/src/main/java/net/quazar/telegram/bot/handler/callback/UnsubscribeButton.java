package net.quazar.telegram.bot.handler.callback;

import lombok.AllArgsConstructor;
import net.quazar.telegram.bot.commands.UnsubscribeCommand;
import net.quazar.telegram.bot.handler.CallbackDataHandler;
import net.quazar.telegram.bot.service.SubscriptionService;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@AllArgsConstructor
@CallbackDataHandler.Handler(callbackData = CallbackDataHandler.Callbacks.UNSUBSCRIBE)
public class UnsubscribeButton implements CallbackDataHandler {
    private final SubscriptionService subscriptionService;

    @Override
    public void handle(Update update, CallbackQuery query, AbsSender absSender) throws TelegramApiException {
        DeleteMessage deleteMessage = DeleteMessage.builder()
                .chatId(query.getMessage().getChatId())
                .messageId(query.getMessage().getMessageId())
                .build();
        absSender.execute(deleteMessage);

        new UnsubscribeCommand(subscriptionService).execute(update, query.getFrom().getId(), absSender);
    }
}
