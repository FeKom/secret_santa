package com.github.fekom.secret_santa.model.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateUserDTO(

        @NotBlank @NotNull(message = "Nome é obrigatório! ") String name,
        @NotBlank @NotNull(message = "O email é obrigatorio! ")  @Email String email,
        @NotBlank @NotNull(message = "A senha é obrigatória! ")
        @Pattern(
                regexp = "(^(?=.*?[0-9]).{8,}$)" ,
                message = "A senha deve possuir no mínimo 8 caracteres, incluindo um numero"
        )
        String password) {
}
