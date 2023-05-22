package net.quazar.telegram.bot.handler;

import feign.FeignException;
import lombok.AllArgsConstructor;
import net.quazar.telegram.bot.Keyboards;
import net.quazar.telegram.bot.commands.CategoriesCommand;
import net.quazar.telegram.bot.commands.TextCommands;
import net.quazar.telegram.bot.commands.UnsubscribeCategoryCommand;
import net.quazar.telegram.proxy.ResourceServerProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@AllArgsConstructor
@StateHandler.Handler(state = StateHandler.State.CATEGORY_UNSUBSCRIBE)
public class UnsubscribeCategoryStateHandler implements StateHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(UnsubscribeCategoryStateHandler.class);

    private final ResourceServerProxy resourceServerProxy;

    @Override
    public int handle(Update update, AbsSender absSender) throws TelegramApiException {
        Message message = update.getMessage();
        ResourceServerProxy.SubscriptionResponse subscription;
        try {
            subscription = resourceServerProxy.getSubscription(message.getFrom().getId()).getBody();
        } catch (FeignException.NotFound e) {
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(message.getFrom().getId())
                    .text("Подписка не найдена")
                    .replyMarkup(Keyboards.getKeyboardByState(State.START))
                    .build();
            absSender.execute(sendMessage);
            return State.START;
        } catch (FeignException e) {
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(message.getFrom().getId())
                    .text("Что-то пошло не так.")
                    .replyMarkup(Keyboards.getKeyboardByState(State.START))
                    .build();
            absSender.execute(sendMessage);
            LOGGER.warn("{} Не удалось получить подписку пользователя: {}", e.status(), e.contentUTF8());
            return State.CATEGORIES_EDIT;
        }

        if (message.getText().equals(TextCommands.RemoveCategoryCommands.DONE)) {
            return new CategoriesCommand(subscription).execute(update, absSender);
        }

        try {
            ResourceServerProxy.CategoryResponse category = resourceServerProxy.unsubscribeCategory(subscription.userId(), message.getText())
                    .getBody();
            subscription.categories().remove(category.name());
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(message.getFrom().getId())
                    .text("Вы отписались от категории " + message.getText())
                    .build();
            absSender.execute(sendMessage);
        } catch (FeignException.NotFound e) {
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(message.getFrom().getId())
                    .text("Не удалось найти категорию " + message.getText())
                    .build();
            absSender.execute(sendMessage);
        }

        return new UnsubscribeCategoryCommand(resourceServerProxy, subscription).execute(update, absSender);
    }
}
