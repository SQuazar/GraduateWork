package net.quazar.resourceserver.repository;

import net.quazar.resourceserver.entity.AnnouncementCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnnouncementCategoryRepository extends JpaRepository<AnnouncementCategory, Integer> {
    Optional<AnnouncementCategory> findByName(@NonNull String name);
}
