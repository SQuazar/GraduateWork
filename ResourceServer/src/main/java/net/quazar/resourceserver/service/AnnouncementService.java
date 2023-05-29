package net.quazar.resourceserver.service;

import net.quazar.resourceserver.entity.Announcement;

import java.util.List;

public interface AnnouncementService {
    Announcement createAnnouncement(String text, int sender, List<String> categories, List<String> roles);
    List<Announcement> getAll();
}
