package com.api.clientes.controller.dto;

import com.api.clientes.model.entity.Person;

public record PersonDto(
    Long id,
    String name,
    String email
) {

  public static PersonDto fromEntity(Person person) {
    return new PersonDto(
        person.getId(),
        person.getName(),
        person.getEmail()
    );
  }
}
