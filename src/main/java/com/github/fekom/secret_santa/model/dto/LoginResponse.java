package com.github.fekom.secret_santa.model.dto;

import java.util.UUID;

public record LoginResponse(String accessToken, String refreshToken, UUID userId) {
}
