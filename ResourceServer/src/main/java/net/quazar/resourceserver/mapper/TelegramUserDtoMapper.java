package net.quazar.resourceserver.mapper;

import net.quazar.resourceserver.entity.TelegramUser;
import net.quazar.resourceserver.entity.dto.TelegramUserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TelegramUserDtoMapper {
    TelegramUserDtoMapper INSTANCE = Mappers.getMapper(TelegramUserDtoMapper.class);

    @Mapping(source = "telegramId", target = "id")
    TelegramUserDto mapToDto(TelegramUser entity);

    @Mapping(source = "id", target = "telegramId")
    TelegramUser mapFromDto(TelegramUserDto dto);
}
