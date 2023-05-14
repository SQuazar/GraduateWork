package net.quazar.apigateway.entity;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TelegramUserDto {
    private final Long id;
    private final List<String> permissions;
}
