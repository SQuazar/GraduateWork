package net.quazar.resourceserver.mapper;

import net.quazar.resourceserver.entity.User;
import net.quazar.resourceserver.entity.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserDtoMapper {
    UserDtoMapper INSTANCE = Mappers.getMapper(UserDtoMapper.class);

    @Mapping(target = "password", ignore = true)
    UserDto userToDto(User user);

    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User dtoToUser(UserDto dto);
}
