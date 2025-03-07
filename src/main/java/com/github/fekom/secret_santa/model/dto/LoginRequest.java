package com.github.fekom.secret_santa.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
       @NotBlank @Email String email,
       @NotBlank String password) {
}
