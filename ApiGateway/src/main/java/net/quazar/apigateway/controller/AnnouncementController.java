package net.quazar.apigateway.controller;

import lombok.AllArgsConstructor;
import net.quazar.apigateway.entity.ApiResponse;
import net.quazar.apigateway.proxy.resource.AnnouncementResponse;
import net.quazar.apigateway.proxy.ResourceServerProxy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {
    private final ResourceServerProxy resourceServerProxy;

    @PreAuthorize("hasAuthority('resource.announcement.save')")
    @PostMapping
    public ResponseEntity<ApiResponse> save(@RequestParam String text,
                                            @RequestParam Optional<List<String>> categories,
                                            @RequestParam Optional<List<String>> roles,
                                            Authentication authentication) {
        var user = resourceServerProxy.getUserByUsername(authentication.getName());
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .code(200)
                        .response(resourceServerProxy.saveAnnouncement(text, user.id(),
                                categories.orElseGet(List::of),
                                roles.orElseGet(List::of)))
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('resource.announcement.get')")
    @GetMapping
    public ResponseEntity<ApiResponse> getAll(@RequestParam Optional<Boolean> sender) {
        if (sender.isPresent()) {
            if (sender.get()) {
                var announcements = resourceServerProxy.getAllAnnouncements()
                        .stream()
                        .map(response -> AnnouncementResponse.AnnouncementWithUserResponse.builder()
                                .id(response.getId())
                                .text(response.getText())
                                .timestamp(response.getTimestamp())
                                .sender(resourceServerProxy.getUserById(response.getSender()))
                                .categories(response.getCategories())
                                .roles(response.getRoles())
                                .build());
                return ResponseEntity.ok(
                        ApiResponse.builder()
                                .code(200)
                                .response(announcements)
                                .build()
                );
            }
        }
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .code(200)
                        .response(resourceServerProxy.getAllAnnouncements())
                        .build()
        );
    }
}
