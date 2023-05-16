package net.quazar.telegram.bot.handler;

import feign.FeignException;
import lombok.AllArgsConstructor;
import net.quazar.telegram.bot.Keyboards;
import net.quazar.telegram.bot.commands.TextCommands;
import net.quazar.telegram.proxy.ResourceServerProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

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
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(message.getFrom().getId())
                    .text(subscription.categories().isEmpty() ?
                            "Вы не подписаны ни на одну из категорий." :
                            "Категории, на которые вы подписаны: " + String.join(", ", subscription.categories()))
                    .replyMarkup(Keyboards.getKeyboardByState(State.CATEGORIES_EDIT))
                    .build();
            absSender.execute(sendMessage);
            return State.CATEGORIES_EDIT;
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

        SendMessage sendMessage = SendMessage.builder()
                .chatId(message.getFrom().getId())
                .text("Выберите категорию, на которую вы хотите подписаться")
                .replyMarkup(getUnsubscribedCategories(subscription))
                .build();
        absSender.execute(sendMessage);
        return State.CATEGORY_SUBSCRIBE;
    }

    private ReplyKeyboardMarkup getUnsubscribedCategories(ResourceServerProxy.SubscriptionResponse subscription) throws FeignException {
        List<ResourceServerProxy.CategoryResponse> categories;
        categories = resourceServerProxy.getCategories().getBody();
        List<KeyboardButton> categoriesButtons = categories
                .stream()
                .filter(category -> !subscription.categories().contains(category.name()))
                .map(category -> new KeyboardButton(category.name()))
                .toList();
        int j = 0;
        ReplyKeyboardMarkup.ReplyKeyboardMarkupBuilder markupBuilder = ReplyKeyboardMarkup.builder();
        KeyboardRow keyboardRow = new KeyboardRow();
        for (KeyboardButton button : categoriesButtons) {
            if (j % 3 == 0) {
                markupBuilder.keyboardRow(keyboardRow);
                keyboardRow = new KeyboardRow();
            }
            keyboardRow.add(button);
            j++;
        }
        markupBuilder.keyboardRow(keyboardRow);
        markupBuilder.keyboardRow(new KeyboardRow(List.of(new KeyboardButton(TextCommands.AddCategoryCommands.DONE))));
        markupBuilder.resizeKeyboard(true);
        return markupBuilder.build();
    }
}
