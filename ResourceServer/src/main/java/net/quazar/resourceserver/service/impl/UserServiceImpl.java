package net.quazar.resourceserver.service.impl;

import lombok.AllArgsConstructor;
import net.quazar.resourceserver.entity.User;
import net.quazar.resourceserver.entity.dto.RoleDto;
import net.quazar.resourceserver.entity.dto.UserDto;
import net.quazar.resourceserver.exception.UserNotFoundException;
import net.quazar.resourceserver.mapper.RoleDtoMapper;
import net.quazar.resourceserver.mapper.UserDtoMapper;
import net.quazar.resourceserver.repository.RoleRepository;
import net.quazar.resourceserver.repository.UserRepository;
import net.quazar.resourceserver.service.UserService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserDtoMapper userDtoMapper = UserDtoMapper.INSTANCE;
    private final RoleDtoMapper roleDtoMapper = RoleDtoMapper.INSTANCE;

    @Override
    public UserDto getById(int userId) {
        return userRepository.findById(userId)
                .map(userDtoMapper::userToDto)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с ID %d не найден".formatted(userId)));
    }

    @Override
    public UserDto getByUsername(@NonNull String username) {
        return userRepository.findByUsername(username)
                .map(userDtoMapper::userToDto)
                .orElseThrow(() -> new UserNotFoundException("Пользователь %s не найден".formatted(username)));
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll()
                .stream()
                .map(userDtoMapper::userToDto)
                .toList();
    }

    @Override
    public List<RoleDto> getRoles(int userId) {
        return roleRepository.findAllByUserId(userId)
                .stream()
                .map(roleDtoMapper::roleToDto)
                .toList();
    }

    @Override
    public Set<String> getAuthorities(int userId) {
        return userRepository.findById(userId)
                .map(User::getAuthorities)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с ID %d не найден".formatted(userId)));
    }

    @Override
    public UserDto save(@NonNull UserDto entity) {
        return userDtoMapper.userToDto(userRepository.save(userDtoMapper.dtoToUser(entity)));
    }

    @Override
    public void delete(int userId) {
        int id = getById(userId).getId();
        userRepository.deleteById(id);
    }
}
