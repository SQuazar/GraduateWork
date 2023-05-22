package net.quazar.telegram.bot.commands;

import lombok.AllArgsConstructor;
import net.quazar.telegram.bot.Keyboards;
import net.quazar.telegram.bot.handler.StateHandler;
import net.quazar.telegram.proxy.ResourceServerProxy;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@AllArgsConstructor
public class CategoriesCommand implements BotCommand {
    private final ResourceServerProxy.SubscriptionResponse subscription;

    @Override
    public int execute(Update update, AbsSender absSender) throws TelegramApiException {
        Message message = update.getMessage();
        SendMessage.SendMessageBuilder messageBuilder = SendMessage.builder()
                .chatId(message.getFrom().getId());
        int state;
        messageBuilder.replyMarkup(Keyboards.getKeyboardByState(state = StateHandler.State.CATEGORIES_EDIT));
        var categories = subscription.categories();
        if (categories == null || categories.isEmpty()) {
            messageBuilder.text("Вы не подписаны ни на одну из категорий.");
            absSender.execute(messageBuilder.build());
            return state;
        }
        messageBuilder.text("Категории, на которые вы подписаны: " + String.join(", ", subscription.categories()));
        absSender.execute(messageBuilder.build());
        return state;
    }
}
