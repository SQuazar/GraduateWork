package net.quazar.resourceserver.service;

import net.quazar.resourceserver.entity.AnnouncementCategory;
import net.quazar.resourceserver.entity.TelegramUser;

public interface TelegramSubscriptionService {
    TelegramUser subscribe(long id);
    void unsubscribe(long id);
    TelegramUser getSubscription(long id);
    TelegramUser changeState(long id, int state);
    AnnouncementCategory subscribeCategory(long id, int categoryId);
    AnnouncementCategory unsubscribeCategory(long id, int categoryId);
}
