package net.quazar.telegram.bot.commands;

import lombok.AllArgsConstructor;
import net.quazar.telegram.bot.Keyboards;
import net.quazar.telegram.bot.handler.StateHandler;
import net.quazar.telegram.bot.service.SubscriptionService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@AllArgsConstructor
public class SubscribeCommand implements BotCommand {
    private final SubscriptionService subscriptionService;

    @Override
    public int execute(Update update, long chatId, AbsSender absSender) throws TelegramApiException {
        if (subscriptionService.hasSubscription(update.getMessage().getFrom().getId())) {
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(chatId)
                    .text("Вы уже подписаны на новостную рассылку")
                    .replyMarkup(Keyboards.getKeyboardByState(StateHandler.State.MENU))
                    .build();
            absSender.execute(sendMessage);
            return StateHandler.State.MENU;
        }

        subscriptionService.subscribe(chatId);
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text("Вы успешно подписались на новостную рассылку")
                .replyMarkup(Keyboards.getKeyboardByState(StateHandler.State.MENU))
                .build();
        absSender.execute(sendMessage);
        return StateHandler.State.MENU;
    }
}
