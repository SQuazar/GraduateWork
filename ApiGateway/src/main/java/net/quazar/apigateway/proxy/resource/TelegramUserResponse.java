package net.quazar.apigateway.proxy.resource;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public final class TelegramUserResponse {
    private final Long id;
    private final String signature;
    @JsonProperty("subscription_date")
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
    private final LocalDateTime subscriptionDate;
    private List<RoleResponse> roles = new ArrayList<>();
    private List<CategoryResponse> categories = new ArrayList<>();
}
