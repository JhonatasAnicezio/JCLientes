package com.api.clientes.controller.dto;

import com.api.clientes.model.entity.Person;

/**
 *  Class for Person Dto.
 *
 * @param id id at Person
 * @param name name at Person
 * @param email email at Person
 */
public record PersonDto(
    Long id,
    String name,
    String email
) {

  /**
   *  Method for convert Person in PersonDto.
   *
   * @param person Person to be converted
   * @return return personDto
   */
  public static PersonDto fromEntity(Person person) {
    return new PersonDto(
        person.getId(),
        person.getName(),
        person.getEmail()
    );
  }
}
