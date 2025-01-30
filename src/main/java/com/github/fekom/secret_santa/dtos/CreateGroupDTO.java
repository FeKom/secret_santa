package com.github.fekom.secret_santa.dtos;

import jakarta.validation.constraints.NotBlank;

public record CreateGroupDTO(
        @NotBlank String name,
                  String Description,
                  String preferences ) {
}
