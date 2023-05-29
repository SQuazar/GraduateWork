package net.quazar.apigateway.controller;

import jakarta.ws.rs.Path;
import lombok.AllArgsConstructor;
import net.quazar.apigateway.entity.ApiResponse;
import net.quazar.apigateway.proxy.ResourceServerProxy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/categories")
public class CategoriesController {
    private final ResourceServerProxy resourceServerProxy;

    @PreAuthorize("hasAnyAuthority('telegram.announcements.send', 'categories.get')")
    @GetMapping
    public ResponseEntity<ApiResponse> getCategories() {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .code(200)
                        .response(resourceServerProxy.getCategories())
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('categories.create')")
    @PutMapping
    public ResponseEntity<ApiResponse> createCategory(@RequestParam String name) {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .code(200)
                        .response(resourceServerProxy.createCategory(name))
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('categories.delete')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable int id) {
        resourceServerProxy.deleteCategory(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .code(200)
                        .response("Категория удалена!")
                        .build()
        );
    }
}
