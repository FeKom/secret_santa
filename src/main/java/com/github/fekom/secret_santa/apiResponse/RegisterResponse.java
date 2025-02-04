package com.github.fekom.secret_santa.apiResponse;

import java.util.UUID;

public record RegisterResponse(String name, UUID userID) {
}
