package net.quazar.resourceserver.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import net.quazar.resourceserver.service.AnnouncementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/announcements")
public class AnnouncementController {
    private final AnnouncementService announcementService;

    @PostMapping
    public ResponseEntity<AnnouncementResponse> save(@RequestParam String text) {
        var entity = announcementService.createAnnouncement(text);
        return ResponseEntity.ok(AnnouncementResponse.builder()
                .id(entity.getId())
                .text(entity.getText())
                .build());
    }

    @Builder
    @Data
    static class AnnouncementResponse {
        private int id;
        private String text;
    }
}
