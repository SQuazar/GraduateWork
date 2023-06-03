package net.quazar.telegram.bot.handler.state;

import feign.FeignException;
import lombok.AllArgsConstructor;
import net.quazar.telegram.bot.Keyboards;
import net.quazar.telegram.bot.commands.CategoriesCommand;
import net.quazar.telegram.bot.commands.SubscribeCategoryCommand;
import net.quazar.telegram.bot.commands.TextCommands;
import net.quazar.telegram.bot.handler.StateHandler;
import net.quazar.telegram.proxy.ResourceServerProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@AllArgsConstructor
@StateHandler.Handler(state = StateHandler.State.CATEGORY_SUBSCRIBE)
public class SubscribeCategoryStateHandler implements StateHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubscribeCategoryStateHandler.class);

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

        if (message.getText().equals(TextCommands.AddCategoryCommands.DONE)) {
            return new CategoriesCommand(subscription).execute(update, absSender);
        }

        try {
            ResourceServerProxy.CategoryResponse category = resourceServerProxy.subscribeCategory(subscription.userId(), message.getText())
                    .getBody();
            subscription.categories().add(category.name());
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(message.getFrom().getId())
                    .text("Вы подписались на категорию " + message.getText())
                    .build();
            absSender.execute(sendMessage);
        } catch (FeignException.NotFound e) {
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(message.getFrom().getId())
                    .text("Не удалось найти категорию " + message.getText())
                    .build();
            absSender.execute(sendMessage);
        }

        return new SubscribeCategoryCommand(resourceServerProxy, subscription).execute(update, absSender);
    }
}
