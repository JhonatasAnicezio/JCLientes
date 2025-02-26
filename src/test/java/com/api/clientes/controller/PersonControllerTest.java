package com.api.clientes.controller;

import com.api.clientes.model.entity.Person;
import com.api.clientes.service.PersonService;
import com.api.clientes.util.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DisplayName("Teste camada controller: PersonController")
public class PersonControllerTest {
  @Autowired
  MockMvc mockMvc;

  @MockBean
  PersonService personService;

  @Test
  @DisplayName("Test requisição POST /clientes")
  public void createPerson() throws Exception {
    Person person = new Person(
        "Xicrinho",
        "xicrinhobolado@gmail.com",
        "12345678",
        Role.ADMIN);

    Person newPerson = new Person(
        person.getName(),
        person.getEmail(),
        person.getPassword(),
        person.getRole()
    );
    newPerson.setId(1L);

    String personJson = "{\n" +
        "\t\"name\": \"Xicrinho\",\n" +
        "  \"email\": \"xicrinhobolado@gmail.com\",\n" +
        "  \"password\": \"12345678\",\n" +
        "  \"role\": \"ADMIN\"\n" +
        "}";

    Mockito.when(personService.create(person))
        .thenReturn(newPerson);

    mockMvc.perform(MockMvcRequestBuilders.post("/clients")
        .contentType(MediaType.APPLICATION_JSON)
        .content(personJson))
        .andExpect(MockMvcResultMatchers.status().isCreated());
  }

  @Test
  @DisplayName("Caso algum parametro esteja faltando")
  public void caseError() throws Exception {
    String personJson = "{\n" +
        "  \"email\": \"xicrinhobolado@gmail.com\",\n" +
        "  \"password\": \"12345678\",\n" +
        "  \"role\": \"ADMIN\"\n" +
        "}";

    mockMvc.perform(MockMvcRequestBuilders.post("/clients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(personJson))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }
}
