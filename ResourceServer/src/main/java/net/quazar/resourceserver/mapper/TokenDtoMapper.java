package net.quazar.resourceserver.mapper;

import net.quazar.resourceserver.entity.Token;
import net.quazar.resourceserver.entity.dto.TokenDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TokenDtoMapper {
    TokenDtoMapper INSTANCE = Mappers.getMapper(TokenDtoMapper.class);

    @Mapping(target = "userId", source = "user.id")
    TokenDto tokenToDto(Token token);
}
