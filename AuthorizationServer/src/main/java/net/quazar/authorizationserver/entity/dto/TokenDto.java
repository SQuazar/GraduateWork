package net.quazar.authorizationserver.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenDto {
    private String token;
    @JsonProperty("user_id")
    private Integer userId;
}
