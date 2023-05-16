package net.quazar.telegram.bot.handler;

import org.springframework.lang.Nullable;

import java.util.List;

public interface StateHandlerRegistry {
    void register(int state, StateHandler handler);
    void unregister(int state);
    @Nullable List<StateHandler> getStateHandlers(int state);
}
