package net.quazar.resourceserver.service.impl;

import lombok.AllArgsConstructor;
import net.quazar.resourceserver.entity.TelegramUser;
import net.quazar.resourceserver.repository.TelegramUserRepository;
import net.quazar.resourceserver.service.TelegramUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class TelegramUserServiceImpl implements TelegramUserService {
    private final TelegramUserRepository telegramUserRepository;

    @Override
    public List<TelegramUser> getAll() {
        return telegramUserRepository.findAll();
    }

    @Override
    public List<TelegramUser> getAllByCategoryId(int categoryId) {
        return telegramUserRepository.findAllByCategoryId(categoryId);
    }

    @Override
    public List<TelegramUser> getAllByRoleId(int roleId) {
        return telegramUserRepository.findAllByRoleId(roleId);
    }

    @Override
    public List<TelegramUser> getAllByRoleIdAndCategoryId(int roleId, int categoryId) {
        return telegramUserRepository.findAllByRoleIdAndCategoryId(roleId, categoryId);
    }
}
