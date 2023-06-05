package net.quazar.telegram.bot.handler.callback;

import lombok.AllArgsConstructor;
import net.quazar.telegram.bot.handler.CallbackDataHandler;
import net.quazar.telegram.bot.service.SubscriptionService;
import net.quazar.telegram.proxy.ResourceServerProxy;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AllArgsConstructor
@CallbackDataHandler.Handler(callbackData =
        {
                CallbackDataHandler.Callbacks.SUBSCRIBE_CATEGORIES,
                CallbackDataHandler.Callbacks.SUBSCRIBE_CATEGORY
        }
)
public class SubscribeCategoriesButton implements CallbackDataHandler {
    private static final Pattern SUBSCRIBE_PATTERN = Pattern.compile("[A-Za-z]+_(\\d+)");

    private final SubscriptionService subscriptionService;

    @Override
    public void handle(Update update, CallbackQuery query, AbsSender absSender) throws TelegramApiException {
        DeleteMessage deleteMessage = DeleteMessage.builder()
                .chatId(query.getMessage().getChatId())
                .messageId(query.getMessage().getMessageId())
                .build();
        absSender.execute(deleteMessage);

        var subscription = subscriptionService.getSubscription(query.getFrom().getId());
        var categories = subscriptionService.getCategories();

        Matcher matcher;
        if ((matcher = SUBSCRIBE_PATTERN.matcher(query.getData())).matches()) {
            try {
                int categoryId = Integer.parseInt(matcher.group(1));
                var category = subscriptionService.subscribeCategory(subscription.userId(), categoryId);
                subscription.categories().add(category.name());

                SendMessage sendMessage = SendMessage.builder()
                        .chatId(query.getMessage().getChatId())
                        .text("Вы подписались на категорию " + category.name())
                        .build();
                absSender.execute(sendMessage);
            } catch (NumberFormatException | IllegalStateException | IndexOutOfBoundsException e) {
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(query.getMessage().getChatId())
                        .text("Неверный ввод")
                        .build();
                absSender.execute(sendMessage);
            }
        }

        SendMessage sendMessage;
        if (categories.stream().allMatch(c -> subscription.categories().contains(c.name()))) {
            sendMessage = SendMessage.builder()
                    .chatId(query.getMessage().getChatId())
                    .text("Вы уже подписаны на все категории")
                    .replyMarkup(InlineKeyboardMarkup.builder()
                            .keyboardRow(List.of(
                                    InlineKeyboardButton.builder()
                                            .text("Вернуться в мои категории")
                                            .callbackData(Callbacks.MY_CATEGORIES)
                                            .build())
                            )
                            .build())
                    .build();
        } else {
            sendMessage = SendMessage.builder()
                    .chatId(query.getMessage().getChatId())
                    .text("Выберите категории, на которые вы хотели бы подписаться:")
                    .replyMarkup(getCategories(subscription, categories))
                    .build();
        }
        absSender.execute(sendMessage);
    }

    private InlineKeyboardMarkup getCategories(ResourceServerProxy.SubscriptionResponse subscription,
                                               List<ResourceServerProxy.CategoryResponse> categories) {
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder markupBuilder = InlineKeyboardMarkup.builder();

        var categoriesButtons = categories
                .stream()
                .filter(c -> !subscription.categories().contains(c.name()))
                .map(c -> InlineKeyboardButton.builder()
                        .text(c.name())
                        .callbackData("subscribeCategory_" + c.id())
                        .build())
                .toList();

        List<InlineKeyboardButton> buttons = new ArrayList<>();
        int j = 0;
        for (InlineKeyboardButton button : categoriesButtons) {
            if (j % 3 == 0) {
                markupBuilder.keyboardRow(buttons);
                buttons = new ArrayList<>();
            }
            buttons.add(button);
            j++;
        }
        markupBuilder.keyboardRow(buttons);
        markupBuilder.keyboardRow(List.of(InlineKeyboardButton.builder()
                .text("Вернуться в мои категории")
                .callbackData(Callbacks.MY_CATEGORIES)
                .build()));

        return markupBuilder.build();
    }
}
