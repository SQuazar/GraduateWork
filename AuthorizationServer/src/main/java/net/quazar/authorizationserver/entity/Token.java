package net.quazar.authorizationserver.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "user_tokens", indexes = {
        @Index(name = "token_index", unique = true, columnList = "token")
})
public class Token {
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Id Integer id;

    @EqualsAndHashCode.Include
    private String token;

    private boolean revoked;
    private long expire;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private TokenType type = TokenType.ACCESS;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public enum TokenType {
        ACCESS,
        REFRESH
    }
}
