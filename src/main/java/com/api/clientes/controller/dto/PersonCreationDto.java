package com.api.clientes.controller.dto;

import com.api.clientes.model.entity.Person;
import com.api.clientes.util.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PersonCreationDto(
    @NotBlank(message = "name is required")
    @Size(min = 3, message = "very short name")
    String name,

    @Email(message = "email must be valid")
    @NotBlank(message = "email is required")
    String email,

    @Size(min = 8, message = "very short password, minimum 8 characters")
    @NotBlank(message = "password is required")
    String password,

    @NotNull(message = "role is required")
    Role role
) {

  public Person toEntity() {
    return new Person(name, email, password, role);
  }
}
