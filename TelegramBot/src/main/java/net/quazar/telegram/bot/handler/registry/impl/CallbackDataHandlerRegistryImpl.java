package net.quazar.telegram.bot.handler.registry.impl;

import net.quazar.telegram.bot.handler.CallbackDataHandler;
import net.quazar.telegram.bot.handler.registry.CallbackDataHandlerRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class CallbackDataHandlerRegistryImpl implements CallbackDataHandlerRegistry {
    private final ConcurrentHashMap<String, List<CallbackDataHandler>> handlersMap = new ConcurrentHashMap<>();

    @Override
    public void register(String callbackData, CallbackDataHandler handler) {
        List<CallbackDataHandler> handlers = this.handlersMap.get(callbackData);
        if (handlers != null) {
            handlers.add(handler);
            return;
        }
        handlers = new ArrayList<>();
        handlers.add(handler);
        this.handlersMap.put(callbackData, handlers);
    }

    @Override
    public void unregister(String callbackData) {
        this.handlersMap.remove(callbackData);
    }

    @Override
    public List<CallbackDataHandler> getHandlers(String callbackData) {
        return this.handlersMap.get(callbackData);
    }
}
