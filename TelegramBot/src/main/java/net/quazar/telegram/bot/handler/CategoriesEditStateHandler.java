package net.quazar.telegram.bot.handler;

import feign.FeignException;
import lombok.AllArgsConstructor;
import net.quazar.telegram.bot.Keyboards;
import net.quazar.telegram.bot.commands.TextCommands;
import net.quazar.telegram.proxy.ResourceServerProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.format.DateTimeFormatter;
import java.util.List;

@AllArgsConstructor
@StateHandler.Handler(state = StateHandler.State.CATEGORIES_EDIT)
public class CategoriesEditStateHandler implements StateHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriesEditStateHandler.class);

    private final ResourceServerProxy resourceServerProxy;
    private final DateTimeFormatter dateTimeFormatter;

    @Override
    public int handle(Update update, AbsSender absSender) throws TelegramApiException {
        Message message = update.getMessage();
        ResponseEntity<ResourceServerProxy.SubscriptionResponse> response;
        try {
            response = resourceServerProxy.getSubscription(message.getFrom().getId());
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
            return State.MENU;
        }
        var subscription = response.getBody();
        switch (message.getText()) {
            case TextCommands.MyCategoriesCommands.GO_BACK -> {
                StringBuilder subscriptionInfo = new StringBuilder("Ваша подписка: \n");
                subscriptionInfo.append("Ваш ID: ").append(subscription.userId()).append('\n')
                        .append("Подписан с: ")
                        .append(subscription.subscriptionDate().format(dateTimeFormatter)).append('\n');
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(message.getFrom().getId())
                        .text(subscriptionInfo.toString())
                        .replyMarkup(Keyboards.getKeyboardByState(State.MY_SUBSCRIPTION))
                        .build();
                absSender.execute(sendMessage);
                return State.MY_SUBSCRIPTION;
            }
            case TextCommands.MyCategoriesCommands.SUBSCRIBE_CATEGORY -> {
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(message.getFrom().getId())
                        .text("Выберите категорию, на которую вы хотите подписаться")
                        .replyMarkup(getUnsubscribedCategories(subscription))
                        .build();
                absSender.execute(sendMessage);
                return State.CATEGORY_SUBSCRIBE;
            }
            case TextCommands.MyCategoriesCommands.UNSUBSCRIBE_CATEGORY -> {
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(message.getFrom().getId())
                        .text("Выберите категорию, от которой вы хотите отписаться")
                        .replyMarkup(getSubscribedCategories(subscription))
                        .build();
                absSender.execute(sendMessage);
                return State.CATEGORY_UNSUBSCRIBE;
            }
            default -> absSender.execute(SendMessage.builder()
                    .chatId(message.getFrom().getId())
                    .text("Команда не найдена")
                    .replyMarkup(Keyboards.getKeyboardByState(State.CATEGORIES_EDIT))
                    .build());
        }
        return State.CATEGORIES_EDIT;
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

    private ReplyKeyboardMarkup getSubscribedCategories(ResourceServerProxy.SubscriptionResponse subscription) throws FeignException {
        List<ResourceServerProxy.CategoryResponse> categories;
        categories = resourceServerProxy.getCategories().getBody();
        List<KeyboardButton> categoriesButtons = categories
                .stream()
                .filter(category -> subscription.categories().contains(category.name()))
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
        markupBuilder.keyboardRow(new KeyboardRow(List.of(new KeyboardButton(TextCommands.RemoveCategoryCommands.DONE))));
        markupBuilder.resizeKeyboard(true);
        return markupBuilder.build();
    }
}
