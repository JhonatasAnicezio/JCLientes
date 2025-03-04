package com.api.clientes.util.validator;

import com.api.clientes.util.Role;
import com.api.clientes.util.anotations.ValidRole;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Locale;

public class RoleValidator implements ConstraintValidator<ValidRole, String>
{
  @Override
  public boolean isValid(String role, ConstraintValidatorContext constraintValidatorContext)
  {
    if (role == null || role.isEmpty()) {
      return false;
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
