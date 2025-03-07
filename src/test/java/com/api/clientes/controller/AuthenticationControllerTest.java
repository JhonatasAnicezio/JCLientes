package com.api.clientes.controller;

import com.api.clientes.model.entity.Person;
import com.api.clientes.service.PersonService;
import com.api.clientes.util.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DisplayName("Teste camada controller: Authentication")
public class AuthenticationControllerTest {
  @Autowired
  MockMvc mockMvc;

  @Autowired
  PersonService personService;

  @Test
  @DisplayName("Test rota /login")
  public void loginTest() throws Exception {
    Person person = new Person(
        "Xicrinho",
        "xicrinhobolado@gmail.com",
        "12345678",
        Role.ADMIN);

    personService.create(person);

    String auth = "{\n" +
        "  \"email\": \"xicrinhobolado@gmail.com\",\n" +
        "  \"password\": \"12345678\"\n" +
        "}";

    mockMvc.perform(MockMvcRequestBuilders.post("/authentication")
            .contentType(MediaType.APPLICATION_JSON)
            .content(auth))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.token").isNotEmpty());
  }

  @Test
  @DisplayName("Testa caso falte parametros para o login")
  public void loginTestFail() throws Exception
  {
    String authNotPassword = "{\n" +
        "  \"email\": \"xicrinhobolado@gmail.com\"\n" +
        "}";

    mockMvc.perform(MockMvcRequestBuilders.post("/authentication")
        .contentType(MediaType.APPLICATION_JSON)
        .content(authNotPassword))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.password")
            .value("Senha é obrigatória"));

    String authNotEmail = "{\n" +
        "  \"password\": \"Xicrinho123!\"\n" +
        "}";

    mockMvc.perform(MockMvcRequestBuilders.post("/authentication")
            .contentType(MediaType.APPLICATION_JSON)
            .content(authNotEmail))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.email")
            .value("Email é obrigatório"));
  }
}
