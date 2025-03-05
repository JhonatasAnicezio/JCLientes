package com.api.clientes.controller;

import com.api.clientes.model.entity.Person;
import com.api.clientes.service.PersonService;
import com.api.clientes.util.Role;
import java.util.List;
import javax.print.attribute.standard.Media;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
  @DisplayName("Test requisição POST /clients")
  public void createPerson() throws Exception {
    Person person = new Person(
        "Xicrinho",
        "xicrinhobolado@gmail.com",
        "Xicrinho123!",
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
        "  \"password\": \"Xicrinho123!\",\n" +
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
  @DisplayName("Test create person com nome invalido")
  public void caseError() throws Exception {
    String personNameNotFound = "{\n" +
        "  \"email\": \"xicrinhobolado@gmail.com\",\n" +
        "  \"password\": \"Xicrinho123!\",\n" +
        "  \"role\": \"ADMIN\"\n" +
        "}";

    String personVeryCut = "{\"name\": \"Jo\", \n" +
        "  \"email\": \"xicrinhobolado@gmail.com\",\n" +
        "  \"password\": \"Xicrinho123!\",\n" +
        "  \"role\": \"ADMIN\"\n" +
        "}";

    mockMvc.perform(MockMvcRequestBuilders.post("/clients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(personNameNotFound))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name")
            .value("O nome é obrigatório"));

    mockMvc.perform(MockMvcRequestBuilders.post("/clients")
        .contentType(MediaType.APPLICATION_JSON)
        .content(personVeryCut))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name")
            .value("O nome é muito curto"));
  }

  @Test
  @DisplayName("Test create person com email invalido")
  public void caseErrorEmail() throws Exception
  {
    String personEmailNotFound = "{\n" +
        "\t\"name\": \"Xicrinho\",\n" +
        "  \"password\": \"Xicrinho123!\",\n" +
        "  \"role\": \"ADMIN\"\n" +
        "}";

    mockMvc.perform(MockMvcRequestBuilders.post("/clients")
        .contentType(MediaType.APPLICATION_JSON)
        .content(personEmailNotFound))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.email")
            .value("O email é obrigatório"));

    String personEmailInvalid = "{\n" +
        "\t\"name\": \"Xicrinho\",\n" +
        "  \"email\": \"xicrinhobolado@\",\n" +
        "  \"password\": \"Xicrinho123!\",\n" +
        "  \"role\": \"ADMIN\"\n" +
        "}";

    mockMvc.perform(MockMvcRequestBuilders.post("/clients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(personEmailInvalid))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.email")
            .value("Formato de email invalido"));
  }

  @Test
  @DisplayName("Test create person com senha invalida")
  public void caseErrorPasswordInvalid() throws Exception
  {
    String personPasswordNotFound = "{\n" +
        "\t\"name\": \"Xicrinho\",\n" +
        "  \"email\": \"xicrinhobolado@\",\n" +
        "  \"role\": \"ADMIN\"\n" +
        "}";

    String personPasswordInvalid = "{\n" +
        "\t\"name\": \"Xicrinho\",\n" +
        "  \"email\": \"xicrinhobolado@\",\n" +
        "  \"password\": \"12345679!\",\n" +
        "  \"role\": \"ADMIN\"\n" +
        "}";


    mockMvc.perform(MockMvcRequestBuilders.post("/clients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(personPasswordNotFound))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.password")
            .value("A senha é obrigatória"));

    mockMvc.perform(MockMvcRequestBuilders.post("/clients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(personPasswordInvalid))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.password")
            .value("A senha deve conter letra maiúscula, minúscula, número e caractere especial."));
  }

  @Test
  @DisplayName("Test create person com role invalido")
  public void caseRoleInvalid() throws Exception
  {
    String personRoleNotFound = "{\n" +
        "\t\"name\": \"Xicrinho\",\n" +
        "  \"email\": \"xicrinhobolado@gmail.com\",\n" +
        "  \"password\": \"Xicrinho123!\"\n" +
        "}";

    String personRoleInvalid = "{\n" +
        "\t\"name\": \"Xicrinho\",\n" +
        "  \"email\": \"xicrinhobolado@gmail.com\",\n" +
        "  \"password\": \"Xicrinho123!\",\n" +
        "  \"role\": \"ADMImim\"\n" +
        "}";

    mockMvc.perform(MockMvcRequestBuilders.post("/clients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(personRoleNotFound))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.role")
            .value("O cago é obrigatório"));

    mockMvc.perform(MockMvcRequestBuilders.post("/clients")
        .contentType(MediaType.APPLICATION_JSON)
        .content(personRoleInvalid))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.role")
            .value("O cargo é invalido"));
  }

  @Test
  @DisplayName("Teste requisição GET /clients")
  public void testGetFindAll() throws Exception {
    Mockito.when(personService.findAll())
            .thenReturn(List.of());

    mockMvc.perform(MockMvcRequestBuilders.get("/clients"))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @DisplayName("Teste requisição DELETE /clients/{id}")
  public void testDelete() throws Exception
  {
    Mockito.when(personService.delete(1L))
            .thenReturn("Suceses!");

    mockMvc.perform(MockMvcRequestBuilders.delete("/clients/1"))
        .andExpect(MockMvcResultMatchers.status().isNoContent());
  }
}
