package net.quazar.apigateway.proxy.resource;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public final class RoleRequest {
    private String name;
    private Set<String> authorities = new HashSet<>();
}
