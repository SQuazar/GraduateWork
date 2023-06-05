package net.quazar.telegram.bot.service;

import net.quazar.telegram.proxy.ResourceServerProxy;

import java.util.List;

public interface SubscriptionService {
    ResourceServerProxy.SubscriptionResponse getSubscription(long userId);

    boolean hasSubscription(long userId);

    void subscribe(long userId);

    void unsubscribe(long userId);

    List<ResourceServerProxy.CategoryResponse> getCategories();

    ResourceServerProxy.CategoryResponse subscribeCategory(long userId, int categoryId);

    ResourceServerProxy.CategoryResponse unsubscribeCategory(long userId, int categoryId);
}
