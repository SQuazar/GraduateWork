package net.quazar.authorizationserver.repository;

import net.quazar.authorizationserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByUsername(@NonNull String username);
    Optional<User> findByUsername(@NonNull String username);
}
