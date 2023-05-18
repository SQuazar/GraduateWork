package net.quazar.resourceserver.controller;

import lombok.AllArgsConstructor;
import net.quazar.resourceserver.entity.dto.RoleDto;
import net.quazar.resourceserver.entity.dto.UserDto;
import net.quazar.resourceserver.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/roles")
public class RoleController {
    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAll());
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

    @PostMapping
    public ResponseEntity<RoleDto> createRole(@RequestParam String name) {
        return ResponseEntity.ok(roleService.save(RoleDto.builder()
                .name(name)
                .build()));
    }

    @PatchMapping("/{id}")
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