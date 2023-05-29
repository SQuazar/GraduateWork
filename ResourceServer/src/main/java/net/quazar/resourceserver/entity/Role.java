package net.quazar.resourceserver.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "roles")
public class Role {
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Id Integer id;

    private @Column(nullable = false, unique = true) String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private @Column(name = "permission") Set<String> permissions;

    @ManyToMany(mappedBy = "roles")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<User> users = new ArrayList<>();

    @ManyToMany(mappedBy = "roles")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<TelegramUser> telegramUsers = new ArrayList<>();
}
