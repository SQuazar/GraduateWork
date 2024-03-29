package net.quazar.resourceserver.repository;

import net.quazar.resourceserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(@NonNull String username);
    @Query(nativeQuery = true,
            value = """
                    SELECT * FROM `users`
                        JOIN `user_roles` ur ON `users`.id = ur.user_id
                    WHERE ur.role_id = :roleId
                    """)
    List<User> findAllByRoleId(int roleId);
}
