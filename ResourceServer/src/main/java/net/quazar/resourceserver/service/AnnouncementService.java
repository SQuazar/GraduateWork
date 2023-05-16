package net.quazar.resourceserver.service;

import net.quazar.resourceserver.entity.Announcement;

public interface AnnouncementService {
    Announcement createAnnouncement(String text);
}
