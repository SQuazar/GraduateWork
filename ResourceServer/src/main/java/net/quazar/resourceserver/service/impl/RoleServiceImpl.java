package net.quazar.resourceserver.service.impl;

import lombok.AllArgsConstructor;
import net.quazar.resourceserver.entity.dto.RoleDto;
import net.quazar.resourceserver.entity.dto.UserDto;
import net.quazar.resourceserver.exception.RoleNotFoundException;
import net.quazar.resourceserver.mapper.RoleDtoMapper;
import net.quazar.resourceserver.mapper.UserDtoMapper;
import net.quazar.resourceserver.repository.RoleRepository;
import net.quazar.resourceserver.repository.UserRepository;
import net.quazar.resourceserver.service.RoleService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final RoleDtoMapper roleDtoMapper = RoleDtoMapper.INSTANCE;
    private final UserDtoMapper userDtoMapper = UserDtoMapper.INSTANCE;

    @Override
    public RoleDto getById(int id) {
        return roleDtoMapper.roleToDto(roleRepository.findById(id).orElseThrow(() ->
                new RoleNotFoundException("Роль с ID %d не найдена".formatted(id))));
    }

    @Override
    public RoleDto getByName(@NonNull String name) {
        return roleDtoMapper.roleToDto(roleRepository.findByName(name).orElseThrow(() ->
                new RoleNotFoundException("Роль '%s' не найдена".formatted(name))));
    }

    @Override
    public List<RoleDto> getAll() {
        return roleRepository.findAll()
                .stream()
                .map(roleDtoMapper::roleToDto)
                .toList();
    }

    @Override
    public List<UserDto> getUsers(int roleId) {
        return userRepository.findAllByRoleId(roleId)
                .stream()
                .map(userDtoMapper::userToDto)
                .toList();
    }

    @Override
    public RoleDto save(@NonNull RoleDto entity) {
        return roleDtoMapper.roleToDto(roleRepository.save(roleDtoMapper.dtoToRole(entity)));
    }

    @Override
    public void delete(int roleId) {
        int id = getById(roleId).getId();
        roleRepository.deleteById(id);
    }
}
