package net.quazar.apigateway.proxy.resource;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UpdateUserRequest {
    private String username;
    private String password;
    private List<Integer> roles = new ArrayList<>();
    private List<String> authorities = new ArrayList<>();
}
