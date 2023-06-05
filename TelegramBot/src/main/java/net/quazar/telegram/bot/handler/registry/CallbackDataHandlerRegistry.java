package net.quazar.telegram.bot.handler.registry;

import net.quazar.telegram.bot.handler.CallbackDataHandler;
import org.springframework.lang.Nullable;

import java.util.List;

public interface CallbackDataHandlerRegistry {
    void register(String callbackData, CallbackDataHandler handler);

    void unregister(String callbackData);

    @Nullable List<CallbackDataHandler> getHandlers(String callbackData);
}
