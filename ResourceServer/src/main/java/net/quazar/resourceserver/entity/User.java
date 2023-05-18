package net.quazar.resourceserver.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "users")
public class User {
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Id Integer id;

    private @Column(nullable = false, unique = true) String username;
    private @Column(nullable = false) String passwordHash;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_permissions",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    @Column(name = "permission")
    private Set<String> permissions = new HashSet<>();

    public Set<String> getAuthorities() {
        Set<String> authorities = new HashSet<>();
        authorities.addAll(permissions);
        authorities.addAll(roles
                .stream()
                .map(Role::getPermissions)
                .flatMap(Collection::stream)
                .toList());
        authorities.addAll(roles
                .stream()
                .map(role -> "ROLE_" + role.getName())
                .toList());
        return authorities;
    }
}
