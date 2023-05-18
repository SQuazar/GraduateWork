package net.quazar.apigateway.controller;

import lombok.AllArgsConstructor;
import net.quazar.apigateway.proxy.resource.AnnouncementDto;
import net.quazar.apigateway.proxy.ResourceServerProxy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {
    private final ResourceServerProxy resourceServerProxy;

    @PreAuthorize("hasAuthority('resource.announcement.save')")
    @PostMapping
    public ResponseEntity<AnnouncementDto> save(@RequestParam String text) {
        return resourceServerProxy.saveAnnouncement(text);
    }
}
