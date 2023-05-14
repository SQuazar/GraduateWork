package net.quazar.apigateway.auth.entity;

public record TokenDto(int id, String token, TokenType type, long expire, boolean revoked) {
    public enum TokenType {
        ACCESS,
        REFRESH
    }
}
