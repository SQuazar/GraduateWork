package net.quazar.resourceserver.service;

import net.quazar.resourceserver.entity.dto.RoleDto;
import net.quazar.resourceserver.entity.dto.UserDto;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Set;

public interface UserService {
    UserDto getById(int userId);
    UserDto getByUsername(@NonNull String username);

    List<UserDto> getAll();

    List<RoleDto> getRoles(int userId);

    Set<String> getAuthorities(int userId);

    UserDto save(@NonNull UserDto entity);

    void delete(int userId);
}
