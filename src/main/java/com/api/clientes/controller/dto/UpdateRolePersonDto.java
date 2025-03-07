package com.api.clientes.controller.dto;

import com.api.clientes.util.anotations.ValidRole;
import jakarta.validation.constraints.NotBlank;

public record UpdateRolePersonDto(
    @NotBlank(message = "O cargo é obrigatório")
    @ValidRole(message = "O cargo é invalido")
    String role
)
{
}
