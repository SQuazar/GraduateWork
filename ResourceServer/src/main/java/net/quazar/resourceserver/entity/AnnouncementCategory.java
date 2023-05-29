package net.quazar.resourceserver.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="announcement_categories")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString(exclude = "users")
@EqualsAndHashCode(exclude = "users")
@Builder
public class AnnouncementCategory {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Id Integer id;
    private @Column(nullable = false, unique = true) String name;
    @ManyToMany(mappedBy = "categories")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<TelegramUser> users = new ArrayList<>();
}
