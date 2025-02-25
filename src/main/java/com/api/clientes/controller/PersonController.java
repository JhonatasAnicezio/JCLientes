package com.api.clientes.controller;

import com.api.clientes.controller.dto.PersonCreationDto;
import com.api.clientes.controller.dto.PersonDto;
import com.api.clientes.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
public class PersonController {
  private final PersonService personService;

  public PersonController(PersonService personService) {
    this.personService = personService;
  }

  @PostMapping
  public ResponseEntity<PersonDto> create(
      @Valid @RequestBody PersonCreationDto personCreationDto) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(PersonDto.fromEntity(personService.create(personCreationDto.toEntity())));
  }
}
