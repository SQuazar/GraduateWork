package net.quazar.apigateway.proxy;

import net.quazar.apigateway.auth.entity.AnnouncementDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "resource-server")
public interface ResourceServerProxy {
    @PostMapping("/announcements")
    ResponseEntity<AnnouncementDto> saveAnnouncement(@RequestParam String text);
}
