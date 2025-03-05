package com.api.clientes.util.validator;

import com.api.clientes.util.Role;
import com.api.clientes.util.anotations.ValidRole;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Locale;

public class RoleValidator implements ConstraintValidator<ValidRole, String>
{
  private String message;

  @Override
  public void initialize(ValidRole constraintAnnotation) {
    this.message = constraintAnnotation.message(); // Pega a mensagem personalizada
  }

  @Override
  public boolean isValid(String role, ConstraintValidatorContext constraintValidatorContext)
  {
    if (role == null || role.isEmpty()) {
      return true;
    }

    try
    {
      Role roleEnum = Role.valueOf(role.toUpperCase());

      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }
}
