package net.quazar.resourceserver.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import net.quazar.resourceserver.entity.TelegramUser;
import net.quazar.resourceserver.service.TelegramUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/telegram/users")
public class TelegramUserController {
    private final TelegramUserService telegramUserService;

    @GetMapping("/category/{categoryId}/ids")
    public ResponseEntity<List<Long>> usersIdsByCategory(@PathVariable int categoryId) {
        return ResponseEntity.ok(telegramUserService.getAllByCategoryId(categoryId)
                .stream()
                .map(TelegramUser::getTelegramId)
                .toList());
    }

    @GetMapping("/role/{roleId}/ids")
    public ResponseEntity<List<Long>> usersIdsByRole(@PathVariable int roleId) {
        return ResponseEntity.ok(telegramUserService.getAllByRoleId(roleId)
                .stream()
                .map(TelegramUser::getTelegramId)
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
                .map(TelegramUser::getTelegramId)
                .toList());
    }

    @GetMapping("/ids")
    public ResponseEntity<List<Long>> getAllUsersIds() {
        return ResponseEntity.ok(telegramUserService.getAll()
                .stream()
                .map(TelegramUser::getTelegramId)
                .toList());
    }

    @Data
    @Builder
    static class TelegramUserSelectionRequest {
        private @JsonProperty("role_id") Integer roleId;
        private @JsonProperty("category_id") Integer categoryId;
    }
}
