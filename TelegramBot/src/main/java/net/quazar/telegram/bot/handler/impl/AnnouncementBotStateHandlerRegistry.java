package net.quazar.telegram.bot.handler.impl;

import net.quazar.telegram.bot.handler.StateHandler;
import net.quazar.telegram.bot.handler.registry.StateHandlerRegistry;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class AnnouncementBotStateHandlerRegistry implements StateHandlerRegistry {
    private final ConcurrentHashMap<Integer, List<StateHandler>> handlersMap = new ConcurrentHashMap<>();

    @Override
    public void register(int state, StateHandler handler) {
        List<StateHandler> handlers = this.handlersMap.get(state);
        if (handlers != null) {
            handlers.add(handler);
            return;
        }
        handlers = new ArrayList<>();
        handlers.add(handler);
        this.handlersMap.put(state, handlers);
    }

    @Override
    public void unregister(int state) {
        this.handlersMap.remove(state);
    }

    @Override
    public @Nullable List<StateHandler> getStateHandlers(int state) {
        return this.handlersMap.get(state);
    }
}
