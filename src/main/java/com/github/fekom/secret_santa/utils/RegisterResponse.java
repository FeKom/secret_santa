package com.github.fekom.secret_santa.utils;

import java.util.UUID;

public record RegisterResponse(String name, UUID userId, String accessToken, String refreshToken) {
}
