package net.quazar.telegram.bot;

import net.quazar.telegram.bot.commands.TextCommands;
import net.quazar.telegram.bot.handler.StateHandler;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

public final class Keyboards {
    public static ReplyKeyboard startKeyboard() {
        return ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(List.of(
                        KeyboardButton.builder()
                                .text(TextCommands.StartCommands.SUBSCRIBE)
                                .build()
                )))
                .resizeKeyboard(true)
                .build();
    }

    public static ReplyKeyboard menuKeyboard() {
        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(
                        new KeyboardRow(List.of(
                                KeyboardButton.builder()
                                        .text(TextCommands.MenuCommands.MY_SUBSCRIPTION)
                                        .build()
                        ))
                ))
                .resizeKeyboard(true)
                .build();
    }

    public static ReplyKeyboard getKeyboardByState(int state) {
        return switch (state) {
            case StateHandler.State.START -> startKeyboard();
            default -> menuKeyboard();
        };
    }
}
