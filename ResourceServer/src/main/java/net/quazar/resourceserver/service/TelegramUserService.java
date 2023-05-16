package net.quazar.resourceserver.service;

import net.quazar.resourceserver.entity.TelegramUser;

import java.util.List;

public interface TelegramUserService {
    List<TelegramUser> getAll();
    List<TelegramUser> getAllByCategoryId(int categoryId);
    List<TelegramUser> getAllByRoleId(int roleId);
    List<TelegramUser> getAllByRoleIdAndCategoryId(int roleId, int categoryId);
}
