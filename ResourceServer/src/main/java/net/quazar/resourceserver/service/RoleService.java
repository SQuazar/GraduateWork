package net.quazar.resourceserver.service;

import net.quazar.resourceserver.entity.dto.RoleDto;
import net.quazar.resourceserver.entity.dto.UserDto;
import org.springframework.lang.NonNull;

import java.util.List;

public interface RoleService {
    RoleDto getById(int id);
    RoleDto getByName(@NonNull String name);
    List<RoleDto> getAll();
    List<UserDto> getUsers(int roleId);
    RoleDto save(@NonNull RoleDto entity);
    void delete(int roleId);
}
