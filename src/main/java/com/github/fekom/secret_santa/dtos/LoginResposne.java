package com.github.fekom.secret_santa.dtos;

import java.util.UUID;

public record LoginResposne(String accessToken, Long expiresIn, UUID userId) {
}
