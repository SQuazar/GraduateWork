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
public class UnsubscribeCommand implements BotCommand {
    private final SubscriptionService subscriptionService;

    @Override
    public int execute(Update update, long chatId, AbsSender absSender) throws TelegramApiException {
        subscriptionService.unsubscribe(chatId);

        int state;
        SendMessage.SendMessageBuilder messageBuilder = SendMessage.builder()
                        .chatId(chatId);
        messageBuilder.replyMarkup(Keyboards.getKeyboardByState(state = StateHandler.State.START));
        messageBuilder.text("Вы отписались от новостной рассылки");
        absSender.execute(messageBuilder.build());
        return state;
    }
}
