package com.api.clientes.controller.dto;

import com.api.clientes.model.entity.Person;
import com.api.clientes.util.Role;
import com.api.clientes.util.anotations.ValidRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 *  Class for created Person.
 *
 * @param name name at Person
 * @param email email at Person
 * @param password password at Person
 * @param role role at Person
 */
public record PersonCreationDto(
    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, message = "O nome é muito curto")
    String name,

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Formato de email invalido")
    String email,

    @Size(min = 8, message = "very short password, minimum 8 characters")
    @NotBlank(message = "A senha é obrigatória")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&]).*$",
        message = "A senha deve conter letra maiúscula, minúscula, número e caractere especial.")
    String password,

    @NotBlank(message = "O cago é obrigatório")
    @ValidRole(message = "O cargo é invalido")
    String role
) {

  /**
   *  Method for convert class in Person.
   *
   * @return return Person
   */
  public Person toEntity() {
    Role role = Role.valueOf(this.role);

    return new Person(name, email, password, role);
  }
}
