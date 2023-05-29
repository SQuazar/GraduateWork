package net.quazar.apigateway.proxy.resource;

import java.util.List;

public record TelegramUserUpdateRequest(String signature, List<Integer> roles, List<Integer> categories) {
}
