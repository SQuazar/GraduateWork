package net.quazar.telegram.bot.handler;

import feign.FeignException;
import lombok.AllArgsConstructor;
import net.quazar.telegram.bot.Keyboards;
import net.quazar.telegram.bot.commands.MySubscriptionCommand;
import net.quazar.telegram.bot.commands.SubscribeCategoryCommand;
import net.quazar.telegram.bot.commands.TextCommands;
import net.quazar.telegram.bot.commands.UnsubscribeCategoryCommand;
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

@StateHandler.Handler(state = StateHandler.State.CATEGORIES_EDIT)
public class CategoriesEditStateHandler implements StateHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriesEditStateHandler.class);

    private final ResourceServerProxy resourceServerProxy;
    private final DateTimeFormatter dateTimeFormatter;
    private final MySubscriptionCommand mySubscriptionCommand;

    public CategoriesEditStateHandler(ResourceServerProxy resourceServerProxy, DateTimeFormatter dateTimeFormatter) {
        this.resourceServerProxy = resourceServerProxy;
        this.dateTimeFormatter = dateTimeFormatter;
        this.mySubscriptionCommand = new MySubscriptionCommand(resourceServerProxy, dateTimeFormatter);
    }

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
                return mySubscriptionCommand.execute(update, absSender);
            }
            case TextCommands.MyCategoriesCommands.SUBSCRIBE_CATEGORY -> {
                return new SubscribeCategoryCommand(resourceServerProxy, subscription).execute(update, absSender);
            }
            case TextCommands.MyCategoriesCommands.UNSUBSCRIBE_CATEGORY -> {
                return new UnsubscribeCategoryCommand(resourceServerProxy, subscription).execute(update, absSender);
            }
            default -> absSender.execute(SendMessage.builder()
                    .chatId(message.getFrom().getId())
                    .text("Команда не найдена")
                    .replyMarkup(Keyboards.getKeyboardByState(State.CATEGORIES_EDIT))
                    .build());
        }
        return State.CATEGORIES_EDIT;
    }
}
