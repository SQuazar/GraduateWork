package net.quazar.resourceserver.service;

import net.quazar.resourceserver.entity.dto.RoleDto;
import net.quazar.resourceserver.entity.dto.UserDto;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Set;

public interface RoleService {
    RoleDto getById(int id);
    List<RoleDto> getAllByIds(List<Integer> ids);
    RoleDto getByName(@NonNull String name);
    List<RoleDto> getAll();
    List<UserDto> getUsers(int roleId);
    Set<String> getAuthorities(int roleId);
    RoleDto save(@NonNull RoleDto entity);
    RoleDto create(@NonNull RoleDto entity);

    void delete(int roleId);
}
