package net.quazar.resourceserver.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import net.quazar.resourceserver.entity.AnnouncementCategory;
import net.quazar.resourceserver.entity.TelegramUser;
import net.quazar.resourceserver.exception.AnnouncementCategoryNotFound;
import net.quazar.resourceserver.exception.TelegramUserNotFoundException;
import net.quazar.resourceserver.repository.AnnouncementCategoryRepository;
import net.quazar.resourceserver.repository.TelegramUserRepository;
import net.quazar.resourceserver.service.TelegramSubscriptionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class TelegramSubscriptionServiceImpl implements TelegramSubscriptionService {
    private final TelegramUserRepository telegramUserRepository;
    private final AnnouncementCategoryRepository categoryRepository;

    @Override
    public TelegramUser subscribe(long id) {
        return telegramUserRepository.findById(id).orElse(
                telegramUserRepository.save(TelegramUser.builder()
                        .telegramId(id)
                        .subscriptionDate(LocalDateTime.now())
                        .build())
        );
    }

    @Override
    public void unsubscribe(long id) {
        telegramUserRepository.deleteById(id);
    }

    @Override
    public TelegramUser getSubscription(long id) {
        return telegramUserRepository.findById(id).orElseThrow(() ->
                new TelegramUserNotFoundException("Подписка не найдена"));
    }

    @Override
    public TelegramUser changeState(long id, int state) {
        var user = getSubscription(id);
        user.setState(state);
        return telegramUserRepository.save(user);
    }

    @Transactional
    @Override
    public AnnouncementCategory subscribeCategory(long id, String categoryName) {
        var user = getSubscription(id);
        var category = categoryRepository.findByName(categoryName).orElseThrow(() ->
                new AnnouncementCategoryNotFound("Category " + categoryName + " isn't found"));
        user.getCategories().add(category);
        telegramUserRepository.save(user);
        return category;
    }

    @Transactional
    @Override
    public AnnouncementCategory unsubscribeCategory(long id, String categoryName) {
        var user = getSubscription(id);
        var category = categoryRepository.findByName(categoryName).orElseThrow(() ->
                new AnnouncementCategoryNotFound("Category " + categoryName + " isn't found"));
        user.getCategories().remove(category);
        telegramUserRepository.save(user);
        return category;
    }
}
