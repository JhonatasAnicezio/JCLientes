package com.api.clientes.controller;

import com.api.clientes.controller.dto.PersonCreationDto;
import com.api.clientes.controller.dto.PersonDto;
import com.api.clientes.model.entity.Person;
import com.api.clientes.service.PersonService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  Person Controller for route /clients.
 */
@RestController
@RequestMapping("/clients")
public class PersonController {
  private final PersonService personService;

  /**
   *  Constructor.
   *
   * @param personService Person Service
   */
  @Autowired
  public PersonController(PersonService personService) {
    this.personService = personService;
  }

  /**
   *  Method for POST /clients.
   *
   * @param personCreationDto param Person for creation
   * @return return Person saved
   */
  @PostMapping
  public ResponseEntity<PersonDto> create(
      @Valid @RequestBody PersonCreationDto personCreationDto) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(PersonDto.fromEntity(personService.create(personCreationDto.toEntity())));
  }

  @GetMapping
  public ResponseEntity<List<PersonDto>> findAll() {
    return ResponseEntity.status(HttpStatus.OK)
        .body(personService.findAll().stream()
            .map(PersonDto::fromEntity)
            .toList());
  }
}
