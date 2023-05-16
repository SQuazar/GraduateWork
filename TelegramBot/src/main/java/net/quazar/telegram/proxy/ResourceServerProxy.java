package net.quazar.telegram.proxy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@FeignClient(name = "resource-server")
public interface ResourceServerProxy {
    @PostMapping("/telegram/subscribe/{id}")
    ResponseEntity<SubscriptionResponse> subscribe(@PathVariable long id);

    @DeleteMapping("/telegram/subscribe/{id}")
    void unsubscribe(@PathVariable long id);

    @GetMapping("/telegram/subscribe/{id}")
    ResponseEntity<SubscriptionResponse> getSubscription(@PathVariable long id);

    @PostMapping("/telegram/subscribe/{id}/state/{state}")
    ResponseEntity<ChangeStateResponse> changeState(@PathVariable long id, @PathVariable int state);

    @PostMapping("/telegram/subscribe/{id}/category/{categoryName}")
    ResponseEntity<CategoryResponse> subscribeCategory(@PathVariable long id, @PathVariable String categoryName);

    @DeleteMapping("/telegram/subscribe/{id}/category/{categoryName}")
    ResponseEntity<CategoryResponse> unsubscribeCategory(@PathVariable long id, @PathVariable String categoryName);

    @GetMapping("/telegram/subscribe/categories")
    ResponseEntity<List<CategoryResponse>> getCategories();

    record SubscriptionResponse(@JsonProperty("user_id") long userId,
                                @JsonProperty("subscription_date") @NonNull LocalDateTime subscriptionDate,
                                List<String> categories, List<String> roles, int state) {
    }

    record ChangeStateResponse(@JsonProperty("user_id") long userId, int state) {
    }

    record CategoryResponse(@JsonProperty("category_id") int id, @JsonProperty("category_name") String name) {
    }

    @GetMapping("/telegram/users/category/{categoryId}/ids")
    ResponseEntity<List<Long>> usersIdsByCategory(@PathVariable int categoryId);

    @GetMapping("/telegram/users/role/{roleId}/ids")
    ResponseEntity<List<Long>> usersIdsByRole(@PathVariable int roleId);

    @GetMapping("/telegram/users/ids")
    ResponseEntity<List<Long>> getAllUsers();

    @GetMapping("/ids/selection")
    ResponseEntity<List<Long>> userIdsBySelection(@RequestBody TelegramUserSelectionRequest request);

    @Builder
    @Data
    public static final class TelegramUserSelectionRequest {
        @JsonProperty("role_id")
        private int roleId;
        @JsonProperty("category_id")
        private int categoryId;
    }
}
