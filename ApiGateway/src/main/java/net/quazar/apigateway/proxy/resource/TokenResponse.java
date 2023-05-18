package net.quazar.apigateway.proxy.resource;

public record TokenResponse(int id, String token, TokenType type, long expire, boolean revoked) {
    public enum TokenType {
        ACCESS,
        REFRESH
    }
}
