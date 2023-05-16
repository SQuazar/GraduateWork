package net.quazar.resourceserver.service.impl;

import lombok.AllArgsConstructor;
import net.quazar.resourceserver.entity.AnnouncementCategory;
import net.quazar.resourceserver.repository.AnnouncementCategoryRepository;
import net.quazar.resourceserver.service.AnnouncementCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class AnnouncementCategoryServiceImpl implements AnnouncementCategoryService {
    private final AnnouncementCategoryRepository categoryRepository;

    @Override
    public List<AnnouncementCategory> getAll() {
        return categoryRepository.findAll();
    }
}
