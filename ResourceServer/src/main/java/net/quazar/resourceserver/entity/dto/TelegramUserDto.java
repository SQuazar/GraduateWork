package net.quazar.resourceserver.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TelegramUserDto {
    private Long id;
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
    private @JsonProperty("subscription_date") LocalDateTime subscriptionDate;
    private String signature;
}
