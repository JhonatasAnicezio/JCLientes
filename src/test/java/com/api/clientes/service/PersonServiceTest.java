package com.api.clientes.service;

import com.api.clientes.controller.dto.PersonDto;
import com.api.clientes.model.entity.Person;
import com.api.clientes.model.repository.PersonRepository;
import com.api.clientes.util.Role;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.mock.MockType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Testes camada service: PersonService")
public class PersonServiceTest {
  @Autowired
  PersonService personService;

  @MockBean
  PersonRepository personRepository;

  @Test
  @DisplayName("Testa cadastro de um novo Person")
  public void testCreate() {
    Person person = new Person(
        "Xicrinho",
        "xicrinhobolado@gmail.com",
        "123456",
        Role.ADMIN);

    Person newPerson = new Person(
        person.getName(),
        person.getEmail(),
        person.getPassword(),
        person.getRole()
    );
    newPerson.setId(1L);

    Mockito.when(personRepository.save(person))
        .thenReturn(newPerson);

    Person savedPerson = personService.create(person);

    Assertions.assertEquals(1L, savedPerson.getId());
    Assertions.assertEquals(person.getName(), savedPerson.getName());
    Assertions.assertEquals(person.getEmail(), savedPerson.getEmail());
    Assertions.assertEquals(person.getPassword(), savedPerson.getPassword());
    Assertions.assertEquals(person.getRole(), savedPerson.getRole());

    Mockito.verify(personRepository).save(person);
  }

  @Test
  @DisplayName("Testa retorno de todos os persons")
  public void testFindAll() {
    Person person1 = new Person(
        "Xicrinho",
        "xicrinhobolado@gmail.com",
        "123456",
        Role.ADMIN);

    Person person2 = new Person(
        "Xicrinho",
        "xicrinhobolado@gmail.com",
        "123456",
        Role.ADMIN);

    Mockito.when(personRepository.findAll())
        .thenReturn(List.of(person1, person2));

    List<Person> persons = personService.findAll();

    Assertions.assertEquals(persons, List.of(person1, person2));
    Mockito.verify(personRepository).findAll();
  }
}
