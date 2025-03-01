package com.api.clientes.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthDto(
    @NotBlank(message = "email is required")
    @Email(message = "email must be valid")
    String email,

    @NotBlank(message = "password is required")
    String password
) {
}
