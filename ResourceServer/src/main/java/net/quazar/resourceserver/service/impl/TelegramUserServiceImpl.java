package net.quazar.resourceserver.service.impl;

import lombok.AllArgsConstructor;
import net.quazar.resourceserver.entity.dto.CategoryDto;
import net.quazar.resourceserver.entity.dto.RoleDto;
import net.quazar.resourceserver.entity.dto.TelegramUserDto;
import net.quazar.resourceserver.exception.TelegramUserNotFoundException;
import net.quazar.resourceserver.mapper.CategoryDtoMapper;
import net.quazar.resourceserver.mapper.RoleDtoMapper;
import net.quazar.resourceserver.mapper.TelegramUserDtoMapper;
import net.quazar.resourceserver.repository.TelegramUserRepository;
import net.quazar.resourceserver.service.AnnouncementCategoryService;
import net.quazar.resourceserver.service.RoleService;
import net.quazar.resourceserver.service.TelegramUserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class TelegramUserServiceImpl implements TelegramUserService {
    private final TelegramUserRepository telegramUserRepository;
    private final RoleService roleService;
    private final AnnouncementCategoryService categoryService;
    private final TelegramUserDtoMapper userDtoMapper = TelegramUserDtoMapper.INSTANCE;
    private final RoleDtoMapper roleDtoMapper = RoleDtoMapper.INSTANCE;
    private final CategoryDtoMapper categoryDtoMapper = CategoryDtoMapper.INSTANCE;

    @Override
    public List<TelegramUserDto> getAll() {
        return telegramUserRepository.findAll()
                .stream()
                .map(userDtoMapper::mapToDto)
                .toList();
    }

    @Override
    public TelegramUserDto getById(Long id) {
        return telegramUserRepository.findById(id)
                .map(userDtoMapper::mapToDto)
                .orElseThrow(() -> new TelegramUserNotFoundException("Пользователь %d не найден".formatted(id)));
    }

    @Override
    public TelegramUserDto update(Long id, String signature, List<Integer> roles, List<Integer> categories) {
        var user = telegramUserRepository.findById(id)
                .orElseThrow(() -> new TelegramUserNotFoundException("Пользователь %d не найден".formatted(id)));
        user.setRoles(roleService.getAllByIds(roles)
                .stream()
                .map(roleDtoMapper::dtoToRole)
                .collect(Collectors.toSet()));
        user.setCategories(categoryService.getAllByIds(categories)
                .stream()
                .map(categoryDtoMapper::mapFromDto)
                .collect(Collectors.toSet()));
        user.setSignature(signature);
        return userDtoMapper.mapToDto(telegramUserRepository.save(user));
    }

    @Override
    public List<TelegramUserDto> getAllByCategoryId(int categoryId) {
        return telegramUserRepository.findAllByCategoryId(categoryId)
                .stream()
                .map(userDtoMapper::mapToDto)
                .toList();
    }

    @Override
    public List<TelegramUserDto> getAllByRoleId(int roleId) {
        return telegramUserRepository.findAllByRoleId(roleId)
                .stream()
                .map(userDtoMapper::mapToDto)
                .toList();
    }

    @Override
    public List<TelegramUserDto> getAllByRoleIdAndCategoryId(int roleId, int categoryId) {
        return telegramUserRepository.findAllByRoleIdAndCategoryId(roleId, categoryId)
                .stream()
                .map(userDtoMapper::mapToDto)
                .toList();
    }

    @Override
    public List<RoleDto> getRolesByUserId(Long id) {
        return telegramUserRepository.findById(id)
                .orElseThrow(() -> new TelegramUserNotFoundException("Пользователь %d не найден".formatted(id)))
                .getRoles()
                .stream()
                .map(roleDtoMapper::roleToDto)
                .toList();
    }

    @Override
    public List<CategoryDto> getCategoriesByUserId(Long id) {
        return telegramUserRepository.findById(id)
                .orElseThrow(() -> new TelegramUserNotFoundException("Пользователь %d не найден".formatted(id)))
                .getCategories()
                .stream()
                .map(categoryDtoMapper::mapToDto)
                .toList();
    }
}
