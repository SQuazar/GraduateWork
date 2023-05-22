package net.quazar.telegram.bot.commands;

import feign.FeignException;
import lombok.AllArgsConstructor;
import net.quazar.telegram.bot.handler.StateHandler;
import net.quazar.telegram.proxy.ResourceServerProxy;
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
public class UnsubscribeCategoryCommand implements BotCommand {
    private final ResourceServerProxy resourceServerProxy;
    private final ResourceServerProxy.SubscriptionResponse subscription;

    @Override
    public int execute(Update update, AbsSender absSender) throws TelegramApiException {
        Message message = update.getMessage();
        SendMessage sendMessage = SendMessage.builder()
                .chatId(message.getFrom().getId())
                .text("Выберите категорию, от которой вы хотите отписаться")
                .replyMarkup(getSubscribedCategories(subscription))
                .build();
        absSender.execute(sendMessage);
        return StateHandler.State.CATEGORY_UNSUBSCRIBE;
    }

    private ReplyKeyboardMarkup getSubscribedCategories(ResourceServerProxy.SubscriptionResponse subscription) throws FeignException {
        List<ResourceServerProxy.CategoryResponse> categories;
        categories = resourceServerProxy.getCategories().getBody();
        assert categories != null;
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
