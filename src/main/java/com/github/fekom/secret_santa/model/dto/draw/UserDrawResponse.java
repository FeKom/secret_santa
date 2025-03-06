package com.github.fekom.secret_santa.model.dto.draw;

import java.util.UUID;

public record UserDrawResponse(UUID userId, String name) {
}
