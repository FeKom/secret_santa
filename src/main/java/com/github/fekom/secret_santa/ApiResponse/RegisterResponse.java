package com.github.fekom.secret_santa.ApiResponse;

import java.util.UUID;

public record RegisterResponse(String name, UUID userID) {
}
