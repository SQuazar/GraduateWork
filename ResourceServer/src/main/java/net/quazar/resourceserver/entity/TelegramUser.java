package net.quazar.resourceserver.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "telegram_users")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString(exclude = "categories")
@EqualsAndHashCode(exclude = "categories")
public class TelegramUser {
    private @Id Long telegramId;
    private @Column(nullable = false) LocalDateTime subscriptionDate;
    @ElementCollection
    private List<String> permissions = new ArrayList<>();
    @ManyToMany
    @JoinTable(name = "subscribed_categories",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<AnnouncementCategory> categories = new ArrayList<>();
}
