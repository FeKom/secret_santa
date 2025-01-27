package com.github.fekom.secret_santa.dtos;

import jakarta.validation.constraints.Email;

public record LoginRequest(
        @Email String email,
        String password) {
}
