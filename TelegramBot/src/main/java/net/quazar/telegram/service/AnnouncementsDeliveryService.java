package net.quazar.telegram.service;

import java.util.Set;

public interface AnnouncementsDeliveryService {
    void startSendingAnnouncement(String text, int category, Set<Integer> roles);
}
