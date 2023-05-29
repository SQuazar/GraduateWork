package net.quazar.resourceserver.mapper;

import net.quazar.resourceserver.entity.AnnouncementCategory;
import net.quazar.resourceserver.entity.dto.CategoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryDtoMapper {
    CategoryDtoMapper INSTANCE = Mappers.getMapper(CategoryDtoMapper.class);

    CategoryDto mapToDto(AnnouncementCategory entity);

    AnnouncementCategory mapFromDto(CategoryDto dto);
}
