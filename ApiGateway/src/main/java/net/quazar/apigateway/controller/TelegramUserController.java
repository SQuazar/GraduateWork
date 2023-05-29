package net.quazar.apigateway.controller;

import lombok.AllArgsConstructor;
import net.quazar.apigateway.entity.ApiResponse;
import net.quazar.apigateway.proxy.ResourceServerProxy;
import net.quazar.apigateway.proxy.resource.TelegramUserUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/telegram/users")
public class TelegramUserController {
    private final ResourceServerProxy resourceServerProxy;

    @PreAuthorize("hasAuthority('telegramusers.get')")
    @GetMapping
    public ResponseEntity<ApiResponse> getAll(@RequestParam Optional<Boolean> roles,
                                              @RequestParam Optional<Boolean> categories) {
        var response = resourceServerProxy.getAllTelegramUsers();
        roles.ifPresent(bool -> {
            if (bool)
                response.forEach(user ->
                        user.setRoles(resourceServerProxy.getTelegramUserRoles(user.getId())));
        });
        categories.ifPresent(bool -> {
            if (bool)
                response.forEach(user ->
                        user.setCategories(resourceServerProxy.getTelegramUserCategories(user.getId())));
        });
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .code(200)
                        .response(response)
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('telegramusers.get')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .code(200)
                        .response(resourceServerProxy.getTelegramUser(id))
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('telegramusers.get')")
    @GetMapping("/{id}/roles")
    public ResponseEntity<ApiResponse> getRoles(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .code(200)
                        .response(resourceServerProxy.getTelegramUserRoles(id))
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('telegramusers.get')")
    @GetMapping("/{id}/categories")
    public ResponseEntity<ApiResponse> getCategories(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .code(200)
                        .response(resourceServerProxy.getTelegramUserCategories(id))
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('telegramusers.update')")
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @RequestBody TelegramUserUpdateRequest request) {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .code(200)
                        .response(resourceServerProxy.updateTelegramUser(id, request))
                        .build()
        );
    }
}
