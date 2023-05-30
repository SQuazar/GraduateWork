package net.quazar.apigateway.proxy;

import net.quazar.apigateway.proxy.resource.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@FeignClient(name = "resource-server")
public interface ResourceServerProxy {
    @PostMapping("/announcements")
    AnnouncementResponse saveAnnouncement(@RequestParam String text, @RequestParam int sender,
                                          @RequestParam List<String> categories,
                                          @RequestParam List<String> roles);

    @GetMapping("/announcements")
    List<AnnouncementResponse> getAllAnnouncements();

    @GetMapping("/roles")
    List<RoleResponse> getAllRoles();

    @GetMapping("/roles/by")
    List<RoleResponse> getAllRolesBy(@RequestParam List<Integer> ids);

    @GetMapping("/roles/{id}")
    RoleResponse getRoleById(@PathVariable int id);

    @GetMapping("/roles/name/{name}")
    RoleResponse getRoleByName(@PathVariable String name);

    @GetMapping("/roles/{id}/users")
    List<UserResponse> getUsersByRole(@PathVariable int id);

    @GetMapping("/roles/{id}/authorities")
    Set<String> getRoleAuthorities(@PathVariable int id);

    @PutMapping("/roles")
    RoleResponse createRole(@RequestParam String name);

    @PostMapping("/roles/{id}")
    RoleResponse updateRole(@PathVariable int id, @RequestBody RoleRequest roleDto);

    @DeleteMapping("/roles/{id}")
    void deleteRole(@PathVariable int id);

    @GetMapping("/users")
    List<UserResponse> getAllUsers();

    @GetMapping("/users/{id}")
    UserResponse getUserById(@PathVariable int id);

    @GetMapping("/users/name/{username}")
    UserResponse getUserByUsername(@PathVariable String username);

    @GetMapping("/users/{id}/authorities")
    Set<String> getUserAuthorities(@PathVariable int id);

    @GetMapping("/users/{id}/permissions")
    Set<String> getUserPermissions(@PathVariable int id);

    @GetMapping("/users/{id}/roles")
    List<RoleResponse> getUserRoles(@PathVariable int id);

    @PutMapping("/users")
    UserResponse createUser(@RequestBody CreateUserRequest request);

    @PostMapping("/users/{id}")
    UserResponse updateUser(@PathVariable int id, @RequestBody UpdateUserRequest request);

    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable int id);

    @GetMapping("/tokens/{token}")
    TokenResponse getToken(@PathVariable String token);

    @GetMapping("/tokens/expired")
    List<TokenResponse> getExpiredTokens();

    @GetMapping("/tokens/revoked")
    List<TokenResponse> getRevokedTokens();

    @GetMapping("/tokens/expired/count")
    int getExpiredTokensCount();
    @GetMapping("/tokens/revoked/count")
    int getRevokedTokensCount();

    @GetMapping("/tokens/user/{userId}")
    List<TokenResponse> getTokensByUserId(@PathVariable int userId);

    @PostMapping("/tokens")
    TokenResponse saveToken(@RequestParam String token, @RequestParam Optional<TokenResponse.TokenType> type, @RequestParam int userId);

    @DeleteMapping("/tokens/{token}")
    void deleteToken(@PathVariable String token);

    @DeleteMapping("/tokens")
    void deleteTokens(@RequestBody List<String> tokens);

    @DeleteMapping("/tokens/expired")
    void deleteExpiredTokens();

    @DeleteMapping("/tokens/revoked")
    void deleteRevokedTokens();

    @GetMapping("/telegram/subscribe/categories")
    List<CategoryResponse> getCategories();

    @GetMapping("/telegram/subscribe/categories")
    List<CategoryResponse> getCategories(@RequestParam List<Integer> ids);

    @GetMapping("/telegram/users")
    List<TelegramUserResponse> getAllTelegramUsers();

    @GetMapping("/telegram/users/{id}")
    TelegramUserResponse getTelegramUser(@PathVariable Long id);

    @GetMapping("/telegram/users/{id}/roles")
    List<RoleResponse> getTelegramUserRoles(@PathVariable Long id);

    @GetMapping("/telegram/users/{id}/categories")
    List<CategoryResponse> getTelegramUserCategories(@PathVariable Long id);

    @PostMapping("/telegram/users/{id}")
    TelegramUserResponse updateTelegramUser(@PathVariable Long id, @RequestBody TelegramUserUpdateRequest request);

    @DeleteMapping("/categories/{id}")
    void deleteCategory(@PathVariable int id);

    @PutMapping("/categories")
    CategoryResponse createCategory(@RequestParam String name);
}