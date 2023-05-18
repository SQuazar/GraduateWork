package net.quazar.apigateway.proxy;

import net.quazar.apigateway.proxy.resource.AnnouncementDto;
import net.quazar.apigateway.proxy.resource.TokenResponse;
import net.quazar.apigateway.proxy.resource.RoleRequest;
import net.quazar.apigateway.proxy.resource.RoleResponse;
import net.quazar.apigateway.proxy.resource.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@FeignClient(name = "resource-server")
public interface ResourceServerProxy {
    @PostMapping("/announcements")
    ResponseEntity<AnnouncementDto> saveAnnouncement(@RequestParam String text);

    @GetMapping("/roles")
    List<RoleResponse> getAllRoles();

    @GetMapping("/roles/{id}")
    RoleResponse getRoleById(@PathVariable int id);

    @GetMapping("/roles/name/{name}")
    RoleResponse getRoleByName(@PathVariable String name);

    @GetMapping("/roles/{id}/users")
    List<UserResponse> getUsersByRole(@PathVariable int id);

    @PostMapping("/roles")
    RoleResponse createRole(@RequestParam String name);

    @PatchMapping("/roles/{id}")
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
    @GetMapping("/users/{id}/roles")
    List<UserResponse> getUserRoles(@PathVariable int id);

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
}