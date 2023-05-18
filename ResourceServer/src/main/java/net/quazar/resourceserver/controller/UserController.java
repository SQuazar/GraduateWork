package net.quazar.resourceserver.controller;

import lombok.AllArgsConstructor;
import net.quazar.resourceserver.entity.dto.RoleDto;
import net.quazar.resourceserver.entity.dto.UserDto;
import net.quazar.resourceserver.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable int id) {
        return userService.getById(id);
    }

    @GetMapping("/name/{username}")
    public UserDto getUserByUsername(@PathVariable String username) {
        return userService.getByUsername(username);
    }

    @GetMapping("/{id}/authorities")
    public Set<String> getUserAuthorities(@PathVariable int id) {
        return userService.getAuthorities(id);
    }

    @GetMapping("/{id}/roles")
    public List<RoleDto> getUserRoles(@PathVariable int id) {
        return userService.getRoles(id);
    }
}
