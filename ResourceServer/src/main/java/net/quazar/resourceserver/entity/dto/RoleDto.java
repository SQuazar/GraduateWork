package net.quazar.resourceserver.entity.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class RoleDto {
    private Integer id;
    private String name;
}
