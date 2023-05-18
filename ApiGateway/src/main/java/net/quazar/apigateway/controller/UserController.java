package net.quazar.apigateway.controller;

import lombok.AllArgsConstructor;
import net.quazar.apigateway.proxy.ResourceServerProxy;
import net.quazar.apigateway.proxy.resource.UserResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final ResourceServerProxy resourceServerProxy;

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return resourceServerProxy.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable int id) {
        return resourceServerProxy.getUserById(id);
    }

    @GetMapping("/name/{username}")
    public UserResponse getUserByUsername(@PathVariable String username) {
        return resourceServerProxy.getUserByUsername(username);
    }

    @GetMapping("/{id}/authorities")
    public Set<String> getUserAuthorities(@PathVariable int id) {
        return resourceServerProxy.getUserAuthorities(id);
    }

    @GetMapping("/{id}/roles")
    public List<UserResponse> getUserRoles(@PathVariable int id) {
        return resourceServerProxy.getUserRoles(id);
    }
}
