package net.quazar.apigateway.proxy.resource;

import lombok.Data;

@Data
public class CreateUserRequest {
    private String username;
    private String password;
}
