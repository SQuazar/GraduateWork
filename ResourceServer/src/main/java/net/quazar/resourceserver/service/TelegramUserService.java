package net.quazar.resourceserver.service;

import net.quazar.resourceserver.entity.dto.CategoryDto;
import net.quazar.resourceserver.entity.dto.RoleDto;
import net.quazar.resourceserver.entity.dto.TelegramUserDto;

import java.util.List;

public interface TelegramUserService {
    List<TelegramUserDto> getAll();
    TelegramUserDto getById(Long id);
    TelegramUserDto update(Long id, String signature, List<Integer> roles, List<Integer> categories);
    List<TelegramUserDto> getAllByCategoryId(int categoryId);
    List<TelegramUserDto> getAllByRoleId(int roleId);

    List<TelegramUserDto> getAllByRoleIdAndCategoryId(int roleId, int categoryId);
    List<RoleDto> getRolesByUserId(Long id);
    List<CategoryDto> getCategoriesByUserId(Long id);
}
