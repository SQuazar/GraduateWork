package net.quazar.resourceserver.mapper;

import net.quazar.resourceserver.entity.Role;
import net.quazar.resourceserver.entity.dto.RoleDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoleDtoMapper {
    RoleDtoMapper INSTANCE = Mappers.getMapper(RoleDtoMapper.class);

    @Mapping(source = "permissions", target = "authorities")
    RoleDto roleToDto(Role role);

    @Mapping(source = "authorities", target = "permissions")
    Role dtoToRole(RoleDto dto);
}
