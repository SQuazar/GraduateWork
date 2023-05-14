package net.quazar.resourceserver.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="announcement_categories")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString(exclude = "users")
@EqualsAndHashCode(exclude = "users")
public class AnnouncementCategory {
    private @Id Integer id;
    private @Column(nullable = false) String name;
    @ManyToMany(mappedBy = "categories")
    private List<TelegramUser> users = new ArrayList<>();
}
