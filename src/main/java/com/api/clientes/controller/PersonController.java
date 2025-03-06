package com.api.clientes.controller;

import com.api.clientes.controller.dto.PersonCreationDto;
import com.api.clientes.controller.dto.PersonDto;
import com.api.clientes.controller.dto.UpdateRolePersonDto;
import com.api.clientes.model.entity.Person;
import com.api.clientes.service.PersonService;
import com.api.clientes.util.Role;
import com.api.clientes.util.anotations.ValidRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

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
  @Secured({ "ADMIN", "MANAGER" })
  public ResponseEntity<List<PersonDto>> findAll() {
    return ResponseEntity.status(HttpStatus.OK)
        .body(personService.findAll().stream()
            .map(PersonDto::fromEntity)
            .toList());
  }

  @GetMapping("/me")
  public ResponseEntity<PersonDto> findByToken(
      HttpServletRequest request
  ) throws UsernameNotFoundException
  {
    Person person = (Person) request.getAttribute("authenticatedUser");

    System.out.println(person);
    if (person == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .build();
    }

    return ResponseEntity.status(HttpStatus.OK)
        .body(PersonDto.fromEntity(person));
  }

  @DeleteMapping("/{id}")
  @Secured({" MANAGER "})
  public ResponseEntity<String> delete(
      @PathVariable Long id
  )
  {
    return ResponseEntity.status(HttpStatus.NO_CONTENT)
        .body(personService.delete(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<PersonDto> updateRole(
      @RequestBody @Valid UpdateRolePersonDto updateRole,
      @PathVariable Long id
  )
  {
    return ResponseEntity.status(HttpStatus.OK)
        .body(PersonDto.fromEntity(personService.updateRole(updateRole.role(), id)));
  }
}
