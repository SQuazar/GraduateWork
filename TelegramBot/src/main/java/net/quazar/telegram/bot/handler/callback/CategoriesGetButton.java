package net.quazar.telegram.bot.handler.callback;

import net.quazar.telegram.bot.commands.CategoriesCommand;
import net.quazar.telegram.bot.handler.CallbackDataHandler;
import net.quazar.telegram.bot.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@CallbackDataHandler.Handler(callbackData = CallbackDataHandler.Callbacks.MY_CATEGORIES)
public class CategoriesGetButton implements CallbackDataHandler {
    private final SubscriptionService subscriptionService;

    @Autowired
    public CategoriesGetButton(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @Override
    public void handle(Update update, CallbackQuery query, AbsSender absSender) throws TelegramApiException {
        DeleteMessage deleteMessage = DeleteMessage.builder()
                .chatId(query.getMessage().getChatId())
                .messageId(query.getMessage().getMessageId())
                .build();
        absSender.execute(deleteMessage);

        var subscription = subscriptionService.getSubscription(query.getFrom().getId());
        new CategoriesCommand(subscription).execute(update, query.getFrom().getId(), absSender);
        // TODO надо решить вопрос, как сделать подписку и отписку от категорий в inline кнопках
        // TODO как вариант это в дату передавать что-то по типу "categorySubscribe:name", а дальше парсить хз
    }
}
