package net.quazar.resourceserver.service.impl;

import lombok.AllArgsConstructor;
import net.quazar.resourceserver.entity.dto.RoleDto;
import net.quazar.resourceserver.entity.dto.UserDto;
import net.quazar.resourceserver.exception.RoleAlreadyExistsException;
import net.quazar.resourceserver.exception.RoleNotFoundException;
import net.quazar.resourceserver.mapper.RoleDtoMapper;
import net.quazar.resourceserver.mapper.UserDtoMapper;
import net.quazar.resourceserver.repository.RoleRepository;
import net.quazar.resourceserver.repository.UserRepository;
import net.quazar.resourceserver.service.RoleService;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

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
    public List<RoleDto> getAllByIds(List<Integer> ids) {
        return roleRepository.findAllById(ids)
                .stream()
                .map(roleDtoMapper::roleToDto)
                .toList();
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
    public Set<String> getAuthorities(int roleId) {
        var role = roleRepository.findById(roleId).orElseThrow(() ->
                new RoleNotFoundException("Роль с ID %d не найдена".formatted(roleId)));
        return role.getPermissions();
    }

    @Override
    public RoleDto save(@NonNull RoleDto entity) {
        roleRepository.findByName(entity.getName()).ifPresent(r -> {
            if (!Objects.equals(r.getId(), entity.getId()))
                throw new RoleAlreadyExistsException("Роль %s уже существует".formatted(r.getName()));
        });
        return roleDtoMapper.roleToDto(roleRepository.save(roleDtoMapper.dtoToRole(entity)));
    }

    @Override
    public RoleDto create(@NonNull RoleDto entity) {
        roleRepository.findByName(entity.getName()).ifPresent(r -> {
            throw new RoleAlreadyExistsException("Роль %s уже существует".formatted(r.getName()));
        });
        return save(entity);
    }

    @Override
    public void delete(int roleId) {
        int id = getById(roleId).getId();
        roleRepository.deleteById(id);
    }
}
