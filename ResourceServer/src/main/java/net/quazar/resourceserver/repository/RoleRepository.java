package net.quazar.resourceserver.repository;

import net.quazar.resourceserver.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(@NonNull String name);

    @Query(nativeQuery = true,
            value = """
                    SELECT * FROM `roles`
                        JOIN `user_roles` ur ON `roles`.id = ur.role_id
                    WHERE ur.user_id = :userId
                    """)
    List<Role> findAllByUserId(int userId);
}
