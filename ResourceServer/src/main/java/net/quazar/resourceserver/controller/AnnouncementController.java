package net.quazar.resourceserver.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import net.quazar.resourceserver.service.AnnouncementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/announcements")
public class AnnouncementController {
    private final AnnouncementService announcementService;

    @PostMapping
    public ResponseEntity<AnnouncementResponse> save(@RequestParam String text, @RequestParam int sender,
                                                     @RequestParam Optional<List<String>> categories, @RequestParam Optional<List<String>> roles) {
        var categoriesList = new ArrayList<String>();
        categories.ifPresent(categoriesList::addAll);
        var rolesList = new ArrayList<String>();
        roles.ifPresent(rolesList::addAll);
        var entity = announcementService.createAnnouncement(text, sender, categoriesList, rolesList);
        return ResponseEntity.ok(AnnouncementResponse.builder()
                .id(entity.getId())
                .text(entity.getText())
                .timestamp(entity.getTimestamp())
                .sender(entity.getSender().getId())
                .build());
    }

    @GetMapping
    public ResponseEntity<List<AnnouncementResponse>> getAll() {
        return ResponseEntity.ok(announcementService.getAll()
                .stream()
                .map(entity -> AnnouncementResponse.builder()
                        .id(entity.getId())
                        .text(entity.getText())
                        .timestamp(entity.getTimestamp())
                        .sender(entity.getSender().getId())
                        .categories(entity.getCategories().isEmpty() ? Set.of() : Arrays.stream(entity.getCategories().split(",")).collect(Collectors.toSet()))
                        .roles(entity.getRoles().isEmpty() ? Set.of() : Arrays.stream(entity.getRoles().split(",")).collect(Collectors.toSet()))
                        .build())
                .toList()
        );
    }

    @Builder
    @Data
    static class AnnouncementResponse {
        private int id;
        private String text;
        @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
        private LocalDateTime timestamp;
        private int sender;
        private Set<String> categories;
        private Set<String> roles;
    }
}
