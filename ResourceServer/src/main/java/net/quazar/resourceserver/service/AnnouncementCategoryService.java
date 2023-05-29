package net.quazar.resourceserver.service;

import net.quazar.resourceserver.entity.dto.CategoryDto;

import java.util.List;

public interface AnnouncementCategoryService {
    List<CategoryDto> getAll();

    List<CategoryDto> getAllByIds(List<Integer> ids);

    CategoryDto create(String name);

    void deleteById(int id);
}
