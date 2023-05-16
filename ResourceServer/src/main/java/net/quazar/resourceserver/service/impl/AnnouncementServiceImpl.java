package net.quazar.resourceserver.service.impl;

import lombok.AllArgsConstructor;
import net.quazar.resourceserver.entity.Announcement;
import net.quazar.resourceserver.repository.AnnouncementRepository;
import net.quazar.resourceserver.service.AnnouncementService;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AnnouncementServiceImpl implements AnnouncementService {
    private final AnnouncementRepository announcementRepository;

    @Override
    public Announcement createAnnouncement(String text) {
        return announcementRepository.save(Announcement.builder()
                .text(text)
                .build());
    }
}
