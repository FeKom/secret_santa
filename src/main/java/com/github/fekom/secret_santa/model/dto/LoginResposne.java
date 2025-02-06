package com.github.fekom.secret_santa.model.dto;

import java.util.UUID;

public record LoginResposne(String accessToken, Long expiresIn, UUID userId) {
}
