package net.quazar.resourceserver.controller;

import lombok.AllArgsConstructor;
import net.quazar.resourceserver.entity.dto.RoleDto;
import net.quazar.resourceserver.entity.dto.UserDto;
import net.quazar.resourceserver.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping("/roles")
public class RoleController {
    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAll());
    }

    @GetMapping("/by")
    public ResponseEntity<List<RoleDto>> getAllRolesBy(@RequestParam Optional<List<Integer>> ids) {
        return ResponseEntity.ok(roleService.getAllByIds(ids.orElse(List.of())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDto> getRoleById(@PathVariable int id) {
        return ResponseEntity.ok(roleService.getById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<RoleDto> getRoleByName(@PathVariable String name) {
        return ResponseEntity.ok(roleService.getByName(name));
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<List<UserDto>> getUsersByRole(@PathVariable int id) {
        return ResponseEntity.ok(roleService.getUsers(id));
    }

    @GetMapping("/{id}/authorities")
    public ResponseEntity<Set<String>> getRoleAuthorities(@PathVariable int id) {
        return ResponseEntity.ok(roleService.getAuthorities(id));
    }

    @PutMapping
    public ResponseEntity<RoleDto> createRole(@RequestParam String name) {
        return ResponseEntity.ok(roleService.create(RoleDto.builder()
                .name(name)
                .build()));
    }

    @PostMapping("/{id}")
    public ResponseEntity<RoleDto> updateRole(@PathVariable int id, @RequestBody RoleDto roleDto) {
        roleDto.setId(id);
        return ResponseEntity.ok(roleService.save(roleDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable int id) {
        roleService.delete(id);
        return ResponseEntity.ok().build();
    }
}