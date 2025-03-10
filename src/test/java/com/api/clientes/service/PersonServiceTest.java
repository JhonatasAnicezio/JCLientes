package com.api.clientes.service;

import com.api.clientes.controller.dto.PersonDto;
import com.api.clientes.model.entity.Person;
import com.api.clientes.model.repository.PersonRepository;
import com.api.clientes.util.Role;
import com.api.clientes.util.exception.PersonNotFoundException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.mock.MockType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Testes camada service: PersonService")
public class PersonServiceTest {
  @Autowired
  PersonService personService;

  @MockBean
  PersonRepository personRepository;

  @MockBean
  JwtService jwtService;

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
    Assertions.assertNotEquals(person.getPassword(), savedPerson.getPassword());
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

  @Test
  @DisplayName("Testa metodo busca usuario por token")
  public void findPersonByTokenTest()
  {
    Person person = new Person(
        "Xicrinho",
        "xicrinhobolado@gmail.com",
        "Xicrinho123!",
        Role.ADMIN);

    Mockito.when(jwtService.getSubject("tokenvalido"))
        .thenReturn("Xicrinho");

    Mockito.when(personRepository.findByEmail("Xicrinho"))
        .thenReturn(Optional.of(person));

    Person personByToken = personService.findByToken("tokenvalido");

    Assertions.assertEquals(person, personByToken);

    Mockito.verify(jwtService).getSubject("tokenvalido");
    Mockito.verify(personRepository).findByEmail("Xicrinho");
  }

  @Test
  @DisplayName("Testa metodo busca por token em caso de erro")
  public void findPersonByTokenTestNotFound()
  {
    Mockito.when(jwtService.getSubject("tokeninvalido"))
        .thenThrow(new JWTDecodeException("Token invalido"));

    Assertions.assertThrows(JWTDecodeException.class,
        () -> personService.findByToken("tokeninvalido"));

    Mockito.verify(jwtService).getSubject("tokeninvalido");
  }

  @Test
  @DisplayName("Testa metodo busca por token caso não tenha usuario")
  public void findPersonByTokenNotFound()
  {
    Mockito.when(jwtService.getSubject("tokenvalido"))
        .thenReturn("Xicrinho");
    Mockito.when(personRepository.findByEmail("Xicrinho"))
        .thenReturn(Optional.empty());

    Assertions.assertThrows(UsernameNotFoundException.class,
        () -> personService.findByToken("tokenvalido"));

    Mockito.verify(jwtService).getSubject("tokenvalido");
    Mockito.verify(personRepository).findByEmail("Xicrinho");
  }

  @Test
  @DisplayName("Teste metodo deletar usuario")
  public void deletePersonTest() {
    Person person1 = new Person(
        1L,
        "Xicrinho",
        "xicrinhobolado@gmail.com",
        "123456",
        Role.ADMIN);

    Mockito.when(personRepository.findById(1L))
        .thenReturn(Optional.of(person1));

    Assertions.assertDoesNotThrow(() -> personService.delete(1L));
    Mockito.verify(personRepository).findById(1L);
  }

  @Test
  @DisplayName("Testa metodo deletar caso usuario não exista")
  public void deletePersonTestNotFound()
  {
    Mockito.when(personRepository.findById(1L))
        .thenReturn(Optional.empty());

    Assertions.assertThrows(PersonNotFoundException.class, () -> personService.delete(1L));

    Mockito.verify(personRepository).findById(1L);
  }

  @Test
  @DisplayName("Testa metodo para atualizar a role")
  public void updateRoleTest() {
    Person person1 = new Person(
        1L,
        "Xicrinho",
        "xicrinhobolado@gmail.com",
        "123456",
        Role.USER);

    Person personNewRole = new Person(
        1L,
        "Xicrinho",
        "xicrinhobolado@gmail.com",
        "123456",
        Role.ADMIN);

    Mockito.when(personRepository.findById(1L))
        .thenReturn(Optional.of(person1));
    Mockito.when(personRepository.save(person1))
        .thenReturn(personNewRole);

    Person person = personService.updateRole("ADMIN", 1L);

    Assertions.assertEquals(personNewRole, person);

    Mockito.verify(personRepository).findById(1L);
    Mockito.verify(personRepository).save(person1);
  }
}
