package net.quazar.telegram.entity.dto;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class TelegramUserDto {
    private final Long id;
    private final List<String> permissions;
}
