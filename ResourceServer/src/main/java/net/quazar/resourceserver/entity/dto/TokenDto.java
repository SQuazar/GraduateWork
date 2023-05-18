package net.quazar.resourceserver.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenDto {
    private int id;
    private String token;
    private boolean revoked;
    private long expire;
    private String type;
    private @JsonProperty("user_id") int userId;
}
