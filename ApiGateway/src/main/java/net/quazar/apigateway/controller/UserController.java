package net.quazar.apigateway.controller;

import lombok.AllArgsConstructor;
import net.quazar.apigateway.entity.ApiResponse;
import net.quazar.apigateway.proxy.ResourceServerProxy;
import net.quazar.apigateway.proxy.resource.CreateUserRequest;
import net.quazar.apigateway.proxy.resource.UpdateUserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@PreAuthorize("hasAuthority('users.get')")
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final ResourceServerProxy resourceServerProxy;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllUsers() {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .code(200)
                        .response(resourceServerProxy.getAllUsers())
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable int id) {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .code(200)
                        .response(resourceServerProxy.getUserById(id))
                        .build()
        );
    }

    @GetMapping("/name/{username}")
    public ResponseEntity<ApiResponse> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .code(200)
                        .response(resourceServerProxy.getUserByUsername(username))
                        .build()
        );
    }

    @GetMapping("/{id}/authorities")
    public ResponseEntity<ApiResponse> getUserAuthorities(@PathVariable int id) {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .code(200)
                        .response(resourceServerProxy.getUserAuthorities(id))
                        .build()
        );
    }

    @GetMapping("/{id}/permissions")
    public ResponseEntity<ApiResponse> getUserPermissions(@PathVariable int id) {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .code(200)
                        .response(resourceServerProxy.getUserPermissions(id))
                        .build()
        );
    }


    @GetMapping("/{id}/roles")
    public ResponseEntity<ApiResponse> getUserRoles(@PathVariable int id) {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .code(200)
                        .response(resourceServerProxy.getUserRoles(id))
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('users.create')")
    @PutMapping
    public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .code(200)
                        .response(resourceServerProxy.createUser(request))
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('users.update')")
    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable int id, @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .code(200)
                        .response(resourceServerProxy.updateUser(id, request))
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('users.delete')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable int id) {
        resourceServerProxy.deleteUser(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .code(200)
                        .response("Пользователь удалён из системы")
                        .build()
        );
    }
}
