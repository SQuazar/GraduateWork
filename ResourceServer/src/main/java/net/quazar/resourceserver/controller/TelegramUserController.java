package net.quazar.resourceserver.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import net.quazar.resourceserver.entity.dto.CategoryDto;
import net.quazar.resourceserver.entity.dto.RoleDto;
import net.quazar.resourceserver.entity.dto.TelegramUserDto;
import net.quazar.resourceserver.service.TelegramUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/telegram/users")
public class TelegramUserController {
    private final TelegramUserService telegramUserService;

    @GetMapping
    public ResponseEntity<List<TelegramUserDto>> getAll() {
        return ResponseEntity.ok(telegramUserService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TelegramUserDto> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(telegramUserService.getById(id));
    }

    @PostMapping("/{id}")
    public ResponseEntity<TelegramUserDto> updateUser(@PathVariable Long id, @RequestBody TelegramUserSaveRequest request) {
        return ResponseEntity.ok(telegramUserService.update(id, request.signature, request.roles, request.categories));
    }

    @GetMapping("/{id}/roles")
    public ResponseEntity<List<RoleDto>> getRoles(@PathVariable Long id) {
        return ResponseEntity.ok(telegramUserService.getRolesByUserId(id));
    }

    @GetMapping("/{id}/categories")
    public ResponseEntity<List<CategoryDto>> getCategories(@PathVariable Long id) {
        return ResponseEntity.ok(telegramUserService.getCategoriesByUserId(id));
    }

    @GetMapping("/category/{categoryId}/ids")
    public ResponseEntity<List<Long>> usersIdsByCategory(@PathVariable int categoryId) {
        return ResponseEntity.ok(telegramUserService.getAllByCategoryId(categoryId)
                .stream()
                .map(TelegramUserDto::getId)
                .toList());
    }

    @GetMapping("/role/{roleId}/ids")
    public ResponseEntity<List<Long>> usersIdsByRole(@PathVariable int roleId) {
        return ResponseEntity.ok(telegramUserService.getAllByRoleId(roleId)
                .stream()
                .map(TelegramUserDto::getId)
                .toList());
    }

    @GetMapping("/ids/selection")
    public ResponseEntity<List<Long>> userIdsBySelection(@RequestBody TelegramUserSelectionRequest request) {
        if (request.roleId == null && request.categoryId == null)
            return getAllUsersIds();
        if (request.categoryId != null && request.roleId == null)
            return usersIdsByCategory(request.categoryId);
       if (request.categoryId == null)
           return usersIdsByRole(request.roleId);
        return ResponseEntity.ok(telegramUserService.getAllByRoleIdAndCategoryId(request.roleId, request.categoryId)
                .stream()
                .map(TelegramUserDto::getId)
                .toList());
    }

    @GetMapping("/ids")
    public ResponseEntity<List<Long>> getAllUsersIds() {
        return ResponseEntity.ok(telegramUserService.getAll()
                .stream()
                .map(TelegramUserDto::getId)
                .toList());
    }

    @Data
    @Builder
    static class TelegramUserSelectionRequest {
        private @JsonProperty("role_id") Integer roleId;
        private @JsonProperty("category_id") Integer categoryId;
    }

    @Data
    static class TelegramUserSaveRequest {
        private String signature;
        private List<Integer> roles;
        private List<Integer> categories;
    }
}
