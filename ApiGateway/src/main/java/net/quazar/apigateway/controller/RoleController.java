package net.quazar.apigateway.controller;

import lombok.AllArgsConstructor;
import net.quazar.apigateway.entity.ApiResponse;
import net.quazar.apigateway.proxy.ResourceServerProxy;
import net.quazar.apigateway.proxy.resource.RoleRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@AllArgsConstructor
@RestController
@RequestMapping("/api/roles")
public class RoleController {
    private final ResourceServerProxy resourceServerProxy;

    @PreAuthorize("hasAnyAuthority('roles.get', 'telegram.announcements.roles')")
    @GetMapping
    public ResponseEntity<ApiResponse> getAllRoles() {
        return ResponseEntity.ok(ApiResponse.builder()
                .code(200)
                .response(resourceServerProxy.getAllRoles())
                .build());
    }

    @PreAuthorize("hasAuthority('roles.get')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getRoleById(@PathVariable int id) {
        return ResponseEntity.ok(ApiResponse.builder()
                .code(200)
                .response(resourceServerProxy.getRoleById(id))
                .build());
    }

    @PreAuthorize("hasAuthority('roles.get')")
    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse> getRoleByName(@PathVariable String name) {
        return ResponseEntity.ok(ApiResponse.builder()
                .code(200)
                .response(resourceServerProxy.getRoleByName(name))
                .build());
    }

    @PreAuthorize("hasAuthority('users.get') && hasAuthority('roles.get')")
    @GetMapping("/{id}/users")
    public ResponseEntity<ApiResponse> getUsersByRole(@PathVariable int id) {
        return ResponseEntity.ok(ApiResponse.builder()
                .code(200)
                .response(resourceServerProxy.getUsersByRole(id))
                .build());
    }

    @PreAuthorize("hasAuthority('roles.get')")
    @GetMapping("/{id}/authorities")
    public ResponseEntity<ApiResponse> getAuthorities(@PathVariable int id) {
        return ResponseEntity.ok(ApiResponse.builder()
                .code(200)
                .response(resourceServerProxy.getRoleAuthorities(id))
                .build()
        );
    }

    @PreAuthorize("hasAuthority('roles.create')")
    @PutMapping
    public ResponseEntity<ApiResponse> createRole(@RequestParam String name) {
        var role = resourceServerProxy.createRole(name);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                        .query(null)
                        .path("/{id}")
                        .buildAndExpand(role.id())
                        .toUri())
                .body(ApiResponse.builder()
                        .code(201)
                        .response(role)
                        .build());
    }

    @PreAuthorize("hasAuthority('roles.update')")
    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse> updateRole(@PathVariable int id, @RequestBody RoleRequest roleDto) {
        return ResponseEntity.ok(ApiResponse.builder()
                .code(200)
                .response(resourceServerProxy.updateRole(id, roleDto))
                .build());
    }

    @PreAuthorize("hasAuthority('roles.delete')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteRole(@PathVariable int id) {
        resourceServerProxy.deleteRole(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .code(200)
                .response("Successfully deleted")
                .build());
    }
}
