package net.quazar.resourceserver.mapper;

import net.quazar.resourceserver.entity.Role;
import net.quazar.resourceserver.entity.dto.RoleDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoleDtoMapper {
    RoleDtoMapper INSTANCE = Mappers.getMapper(RoleDtoMapper.class);

    RoleDto roleToDto(Role role);

    Role dtoToRole(RoleDto dto);
}
