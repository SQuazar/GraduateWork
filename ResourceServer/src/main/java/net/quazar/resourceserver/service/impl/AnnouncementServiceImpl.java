package net.quazar.resourceserver.service.impl;

import lombok.AllArgsConstructor;
import net.quazar.resourceserver.entity.Announcement;
import net.quazar.resourceserver.exception.UserNotFoundException;
import net.quazar.resourceserver.repository.AnnouncementRepository;
import net.quazar.resourceserver.repository.UserRepository;
import net.quazar.resourceserver.service.AnnouncementService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class AnnouncementServiceImpl implements AnnouncementService {
    private final AnnouncementRepository announcementRepository;
    private final UserRepository userRepository;

    @Override
    public Announcement createAnnouncement(String text, int sender, List<String> categories, List<String> roles) {
        var user = userRepository.findById(sender)
                .orElseThrow(() -> new UserNotFoundException("Пользователь %d не найден".formatted(sender)));
        return announcementRepository.save(Announcement.builder()
                .text(text)
                .sender(user)
                .categories(String.join(",", categories))
                .roles(String.join(",", roles))
                .build());
    }

    @Override
    public List<Announcement> getAll() {
        return announcementRepository.findAll(Sort.by(Sort.Direction.DESC, "timestamp"));
    }
}
