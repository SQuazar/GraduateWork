package net.quazar.resourceserver.service.impl;

import lombok.AllArgsConstructor;
import net.quazar.resourceserver.entity.AnnouncementCategory;
import net.quazar.resourceserver.entity.dto.CategoryDto;
import net.quazar.resourceserver.exception.AnnouncementCategoryAlreadyExists;
import net.quazar.resourceserver.exception.AnnouncementCategoryNotFound;
import net.quazar.resourceserver.mapper.CategoryDtoMapper;
import net.quazar.resourceserver.repository.AnnouncementCategoryRepository;
import net.quazar.resourceserver.service.AnnouncementCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class AnnouncementCategoryServiceImpl implements AnnouncementCategoryService {
    private final AnnouncementCategoryRepository categoryRepository;
    private final CategoryDtoMapper mapper = CategoryDtoMapper.INSTANCE;

    @Override
    public List<CategoryDto> getAll() {
        return categoryRepository.findAll()
                .stream()
                .map(mapper::mapToDto)
                .toList();
    }

    @Override
    public List<CategoryDto> getAllByIds(List<Integer> ids) {
        return categoryRepository.findAllById(ids)
                .stream()
                .map(mapper::mapToDto)
                .toList();
    }

    @Override
    public CategoryDto create(String name) {
        categoryRepository.findByName(name).ifPresent(res -> {
            throw new AnnouncementCategoryAlreadyExists("Категория '%s' уже существует".formatted(name));
        });
        return mapper.mapToDto(categoryRepository.save(AnnouncementCategory.builder()
                .name(name)
                .build()));
    }

    @Override
    public void deleteById(int id) {
        var findId = categoryRepository.findById(id)
                .orElseThrow(() -> new AnnouncementCategoryNotFound("Категория с ID %d не найдена".formatted(id)))
                .getId();
        categoryRepository.deleteById(findId);
    }
}
