package net.quazar.resourceserver.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "telegram_user_roles",
            joinColumns = @JoinColumn(name = "telegram_user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    @ManyToMany
    @JoinTable(name = "subscribed_categories",
            joinColumns = @JoinColumn(name = "telegram_user_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<AnnouncementCategory> categories = new HashSet<>();
    private int state = 0;
    private String signature;
}
