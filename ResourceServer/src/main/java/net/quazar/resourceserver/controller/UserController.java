package net.quazar.resourceserver.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.quazar.resourceserver.entity.dto.RoleDto;
import net.quazar.resourceserver.entity.dto.UserDto;
import net.quazar.resourceserver.service.UserService;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @GetMapping("/{id}/permissions")
    public Set<String> getUserPermissions(@PathVariable int id) {
        return userService.getPermissions(id);
    }

    @GetMapping("/{id}/roles")
    public List<RoleDto> getUserRoles(@PathVariable int id) {
        return userService.getRoles(id);
    }

    @PutMapping
    public UserDto createUser(@RequestBody CreateUserRequest request) {
        return userService.createUser(request.username, request.password);
    }

    @PostMapping("/{id}")
    public UserDto updateUser(@PathVariable int id, @RequestBody UpdateUserRequest request) {
        return userService.update(id, request.username, request.password, request.roles, request.authorities);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        userService.delete(id);
    }

    @Data
    static class CreateUserRequest {
        private @NonNull String username;
        private @NonNull String password;
    }

    @Data
    static class UpdateUserRequest {
        private @NonNull String username;
        private @NonNull String password;
        private @NonNull List<Integer> roles = new ArrayList<>();
        private @NonNull List<String> authorities = new ArrayList<>();
    }
}
