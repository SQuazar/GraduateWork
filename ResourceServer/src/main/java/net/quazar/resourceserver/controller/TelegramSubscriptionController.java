package net.quazar.resourceserver.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import net.quazar.resourceserver.entity.AnnouncementCategory;
import net.quazar.resourceserver.entity.Role;
import net.quazar.resourceserver.service.AnnouncementCategoryService;
import net.quazar.resourceserver.service.TelegramSubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/telegram/subscribe")
public class TelegramSubscriptionController {
    private final TelegramSubscriptionService subscriptionService;
    private final AnnouncementCategoryService categoryService;

    @PostMapping("/{id}")
    public ResponseEntity<SubscriptionResponse> subscribe(@PathVariable long id) {
        var user = subscriptionService.subscribe(id);
        return ResponseEntity.ok(SubscriptionResponse.builder()
                .userId(user.getTelegramId())
                .subscriptionDate(user.getSubscriptionDate())
                .build());
    }

    @DeleteMapping("/{id}")
    public void unsubscribe(@PathVariable long id) {
        subscriptionService.unsubscribe(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionResponse> getSubscription(@PathVariable long id) {
        var user = subscriptionService.getSubscription(id);
        return ResponseEntity.ok(SubscriptionResponse.builder()
                .userId(user.getTelegramId())
                .subscriptionDate(user.getSubscriptionDate())
                .categories(user.getCategories()
                        .stream()
                        .map(AnnouncementCategory::getName)
                        .toList())
                .roles(user.getRoles()
                        .stream()
                        .map(Role::getName)
                        .toList())
                .state(user.getState())
                .build());
    }

    @PostMapping("/{id}/state/{state}")
    public ResponseEntity<ChangeStateResponse> setState(@PathVariable long id, @PathVariable int state) {
        var user = subscriptionService.changeState(id, state);
        return ResponseEntity.ok(ChangeStateResponse.builder()
                .userId(user.getTelegramId())
                .state(user.getState())
                .build());
    }

    @PostMapping("/{id}/category/{categoryId}")
    public ResponseEntity<CategoryResponse> subscribeCategory(@PathVariable long id, @PathVariable int categoryId) {
        var category = subscriptionService.subscribeCategory(id, categoryId);
        return ResponseEntity.ok(CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build());
    }

    @DeleteMapping("/{id}/category/{categoryId}")
    public ResponseEntity<CategoryResponse> unsubscribeCategory(@PathVariable long id, @PathVariable int categoryId) {
        var category = subscriptionService.unsubscribeCategory(id, categoryId);
        return ResponseEntity.ok(CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build());
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponse>> getCategories(@RequestParam Optional<List<Integer>> ids) {
        return ResponseEntity.ok((ids.isPresent() ? categoryService.getAllByIds(ids.get()) : categoryService.getAll())
                .stream()
                .map(category -> CategoryResponse.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .build()
                )
                .toList());
    }

    @Data
    @Builder
    static class SubscriptionResponse {
        private @JsonProperty("user_id") long userId;
        private @JsonProperty("subscription_date") LocalDateTime subscriptionDate;
        private @JsonProperty("categories") List<String> categories;
        private @JsonProperty("roles") List<String> roles;
        private int state;
    }

    @Data
    @Builder
    static class ChangeStateResponse {
        private @JsonProperty("user_id") long userId;
        private int state;
    }

    @Data
    @Builder
    static class CategoryResponse {
        private int id;
        private String name;
    }
}
