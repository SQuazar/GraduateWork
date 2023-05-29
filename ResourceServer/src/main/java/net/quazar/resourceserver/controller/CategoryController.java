package net.quazar.resourceserver.controller;

import lombok.AllArgsConstructor;
import net.quazar.resourceserver.entity.dto.CategoryDto;
import net.quazar.resourceserver.service.AnnouncementCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final AnnouncementCategoryService service;
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<CategoryDto> create(@RequestParam String name) {
        return ResponseEntity.ok(service.create(name));
    }
}
