package net.quazar.apigateway.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse {
    private int code;
    private Object response;
}
