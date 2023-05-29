package net.quazar.resourceserver.entity.dto;

import lombok.Builder;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public final class RoleDto {
    private Integer id;
    private String name;
    @Builder.Default
    private final Set<String> authorities = new HashSet<>();
}
