package net.quazar.resourceserver.entity;

import jakarta.persistence.*;
import jakarta.ws.rs.DefaultValue;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "announcements")
public class Announcement {
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Id Integer id;
    private @Lob String text;
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
    @OneToOne
    private User sender;
    @Builder.Default
    @Column(nullable = false)
    @DefaultValue("''")
    private String categories = "";
    @Builder.Default
    @Column(nullable = false)
    @DefaultValue("''")
    private String roles = "";
}
