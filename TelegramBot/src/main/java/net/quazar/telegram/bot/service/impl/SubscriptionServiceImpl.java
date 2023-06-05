package net.quazar.telegram.bot.service.impl;

import feign.FeignException;
import lombok.AllArgsConstructor;
import net.quazar.telegram.bot.AnnouncementsBot;
import net.quazar.telegram.bot.exception.SomethingIsWrongException;
import net.quazar.telegram.bot.exception.SubscriptionNotFoundException;
import net.quazar.telegram.bot.handler.StateHandler;
import net.quazar.telegram.bot.service.SubscriptionService;
import net.quazar.telegram.proxy.ResourceServerProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionServiceImpl.class);

    private final ResourceServerProxy resourceServerProxy;
    private final AnnouncementsBot bot;

    @Override
    public ResourceServerProxy.SubscriptionResponse getSubscription(long userId) {
        try {
            return resourceServerProxy.getSubscription(userId).getBody();
        } catch (FeignException.NotFound e) {
            bot.changeSubscriberState(userId, StateHandler.State.START);
            throw new SubscriptionNotFoundException("Подписка не найдена");
        } catch (FeignException e) {
            LOGGER.warn("{} Cannot get user subscription from resource server: {}", e.status(), e.contentUTF8());
            bot.changeSubscriberState(userId, StateHandler.State.MENU);
            throw new SomethingIsWrongException("Что-то пошло не так");
        }
    }

    @Override
    public void unsubscribe(long userId) {
        try {
            resourceServerProxy.unsubscribe(userId);
        } catch (FeignException.NotFound e) {
            bot.changeSubscriberState(userId, StateHandler.State.START);
            throw new SubscriptionNotFoundException("Подписка не найдена");
        } catch (FeignException e) {
            LOGGER.warn("{} Cannot get user subscription from resource server: {}", e.status(), e.contentUTF8());
            bot.changeSubscriberState(userId, StateHandler.State.MENU);
            throw new SomethingIsWrongException("Что-то пошло не так");
        }
    }

    @Override
    public List<ResourceServerProxy.CategoryResponse> getCategories() {
        try {
            return resourceServerProxy.getCategories().getBody();
        } catch (FeignException e) {
            LOGGER.warn("{} Cannot get categories from resource server: {}", e.status(), e.contentUTF8());
            throw new SomethingIsWrongException("Что-то пошло не так");
        }
    }

    @Override
    public ResourceServerProxy.CategoryResponse subscribeCategory(long userId, int categoryId) {
        try {
            return resourceServerProxy.subscribeCategory(userId, categoryId).getBody();
        } catch (FeignException.NotFound e) {
            bot.changeSubscriberState(userId, StateHandler.State.MENU);
            throw new SomethingIsWrongException("Категория не найдена");
        } catch (FeignException e) {
            LOGGER.warn("{} Cannot subscribe user [{}] from category [{}]: {}", e.status(), userId, categoryId, e.contentUTF8());
            throw new SomethingIsWrongException("Что-то пошло не так");
        }
    }

    @Override
    public ResourceServerProxy.CategoryResponse unsubscribeCategory(long userId, int categoryId) {
        try {
            return resourceServerProxy.unsubscribeCategory(userId, categoryId).getBody();
        } catch (FeignException.NotFound e) {
            bot.changeSubscriberState(userId, StateHandler.State.MENU);
            throw new SomethingIsWrongException("Категория не найдена");
        } catch (FeignException e) {
            LOGGER.warn("{} Cannot unsubscribe user [{}] from category [{}]: {}", e.status(), userId, categoryId, e.contentUTF8());
            throw new SomethingIsWrongException("Что-то пошло не так");
        }
    }
}
