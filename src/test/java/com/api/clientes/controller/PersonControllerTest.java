package com.api.clientes.controller;

import com.api.clientes.model.entity.Person;
import com.api.clientes.service.PersonService;
import com.api.clientes.util.Role;
import com.api.clientes.util.exception.PersonNotFoundException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DisplayName("Teste camada controller: PersonController")
public class PersonControllerTest
{
  @Autowired
  MockMvc mockMvc;

  @MockBean
  PersonService personService;

  @Test
  @DisplayName("Test requisição POST /clients")

  public void createPerson() throws Exception
  {
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

    Mockito.verify(personService).create(person);
  }

  @Test
  @DisplayName("Test create person com nome invalido")
  public void caseError() throws Exception
  {
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
            .value("O cargo é obrigatório"));

    mockMvc.perform(MockMvcRequestBuilders.post("/clients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(personRoleInvalid))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.role")
            .value("O cargo é invalido"));
  }

  @Test
  @DisplayName("Teste requisição como ADMIN GET /clients")
  public void testAdminGetFindAll() throws Exception
  {
    Person personAdmin = new Person();
    personAdmin.setName("XicrinhoAdmin");
    personAdmin.setRole(Role.ADMIN);

    Mockito.when(personService.findByToken("tokenadmin"))
            .thenReturn(personAdmin);

    Mockito.when(personService.findAll())
        .thenReturn(List.of());

    mockMvc.perform(MockMvcRequestBuilders.get("/clients")
            .header(HttpHeaders.AUTHORIZATION, "Bearer tokenadmin"))
        .andExpect(MockMvcResultMatchers.status().isOk());

    Mockito.verify(personService).findAll();
  }

  @Test
  @DisplayName("Teste requisição como MANAGER GET /clients")
  public void testManagerGetFindAll() throws Exception {
    Person personManager = new Person();
    personManager.setName("XicrinhoManager");
    personManager.setRole(Role.MANAGER);

    Mockito.when(personService.findByToken("tokenmanager"))
        .thenReturn(personManager);

    Mockito.when(personService.findAll())
        .thenReturn(List.of());

    mockMvc.perform(MockMvcRequestBuilders.get("/clients")
        .header(HttpHeaders.AUTHORIZATION, "Bearer tokenmanager"))
        .andExpect(MockMvcResultMatchers.status().isOk());

    Mockito.verify(personService).findAll();
  }

  @Test
  @DisplayName("Teste requisição caso usuario não seja ADMIN GET /clients")
  public void testUserGetFindAll() throws Exception
  {
    Person person = new Person();

    person.setName("XicrinhoAdmin");
    person.setRole(Role.USER);

    Mockito.when(personService.findByToken(Mockito.anyString()))
        .thenReturn(person);

    mockMvc.perform(MockMvcRequestBuilders.get("/clients")
            .header(HttpHeaders.AUTHORIZATION, "Bearer tokeninvalido"))
        .andExpect(MockMvcResultMatchers.status().isForbidden());
  }

  @Test
  @DisplayName("Teste requisição GET /clients/me")
  public void testGetFindMe() throws Exception
  {
    Person person = new Person(
        1L,
        "Xicrinho",
        "xicrinhobolado@gmail.com",
        "Xicrinho123!",
        Role.ADMIN);

    Mockito.when(personService.findByToken(Mockito.anyString()))
        .thenReturn(person);

    mockMvc.perform(MockMvcRequestBuilders.get("/clients/me")
            .header("Authorization", "Bearer tokenvalido"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Xicrinho"));

    Mockito.verify(personService).findByToken(Mockito.anyString());
  }

  @Test
  @DisplayName("Teste requisição caso falte o header GET /clients/me")
  public void testGetFindMeHeaderNotFound() throws Exception
  {
    mockMvc.perform(MockMvcRequestBuilders.get("/clients/me"))
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  @Test
  @DisplayName("Teste requisição caso falte o token GET /client/me")
  public void testGetFindMeTokenNotFound() throws Exception
  {
    mockMvc.perform(MockMvcRequestBuilders.get("/clients/me")
            .header(HttpHeaders.AUTHORIZATION, "Bearer "))
        .andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  @Test
  @DisplayName("Teste requisição caso token invalido GET /clients/me")
  public void testGetFindMeTokenInvalid() throws Exception
  {
    Mockito.when(personService.findByToken("tokeninvalido"))
        .thenThrow(new JWTDecodeException("Token invalido"));

    mockMvc.perform(MockMvcRequestBuilders.get("/clients/me")
            .header(HttpHeaders.AUTHORIZATION, "Bearer tokeninvalido"))
        .andExpect(MockMvcResultMatchers.status().isUnauthorized())
        .andExpect(MockMvcResultMatchers.content().string("Token invalido"));

    Mockito.verify(personService).findByToken("tokeninvalido");
  }

  @Test
  @DisplayName("Teste requisição caso usuario não exista GET /clients/me")
  public void testGetFindMeUserNotFound() throws Exception
  {
    Mockito.when(personService.findByToken("tokensemusuario"))
        .thenThrow(new UsernameNotFoundException("Usuario não encontrado!"));

    mockMvc.perform(MockMvcRequestBuilders.get("/clients/me")
            .header(HttpHeaders.AUTHORIZATION, "Bearer tokensemusuario"))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.content().string("Usuario não encontrado!"));

    Mockito.verify(personService).findByToken("tokensemusuario");
  }

  @Test
  @DisplayName("Teste requisição PUT /clients/{id}")
  public void testPutRole() throws Exception
  {
    Person personManager = new Person();
    personManager.setId(1L);
    personManager.setName("Xicrinho");
    personManager.setRole(Role.MANAGER);

    Mockito.when(personService.findByToken(Mockito.anyString()))
        .thenReturn(personManager);

    Person person = new Person();
    person.setId(1L);
    person.setName("Xicrinho");
    person.setEmail("<EMAIL>");
    person.setPassword("<PASSWORD>");
    person.setRole(Role.ADMIN);

    Mockito.when(personService.updateRole("ADMIN", 1L))
        .thenReturn(person);

    mockMvc.perform(MockMvcRequestBuilders.put("/clients/1")
            .header(HttpHeaders.AUTHORIZATION, "Bearer tokenmanager")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\n" +
                "  \"role\": \"ADMIN\"\n" +
                "}"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Xicrinho"));

    Mockito.verify(personService).updateRole("ADMIN", 1L);
  }

  @Test
  @DisplayName("Teste requisição sem role PUT /clients/{id}")
  public void testPutRoleNotFound() throws Exception
  {
    Person personManager = new Person();
    personManager.setId(1L);
    personManager.setName("Xicrinho");
    personManager.setRole(Role.MANAGER);

    Mockito.when(personService.findByToken(Mockito.anyString()))
        .thenReturn(personManager);

    mockMvc.perform(MockMvcRequestBuilders.put("/clients/1")
            .header(HttpHeaders.AUTHORIZATION, "Bearer tokenmanager")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\n" +
                "  \"role\": \"\"\n" +
                "}"))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.role")
            .value("O cargo é obrigatório"));
  }

  @Test
  @DisplayName("Teste requisição com role invalido PUT /clients/{id}")
  public void testPutRoleInvalid() throws Exception
  {
    Person personManager = new Person();
    personManager.setId(1L);
    personManager.setName("Xicrinho");
    personManager.setRole(Role.MANAGER);

    Mockito.when(personService.findByToken(Mockito.anyString()))
        .thenReturn(personManager);

    mockMvc.perform(MockMvcRequestBuilders.put("/clients/1")
            .header(HttpHeaders.AUTHORIZATION, "Bearer tokenmanager")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\n" +
                "  \"role\": \"PODEROSOCHEFAO\"\n" +
                "}"))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.role")
            .value("O cargo é invalido"));
  }

  @Test
  @DisplayName("Teste requisição em caso de usuario não ser encotrado PUT /clients/{id}")
  public void testPutRoleUserNotFound() throws Exception
  {
    Person personManager = new Person();
    personManager.setId(1L);
    personManager.setName("Xicrinho");
    personManager.setRole(Role.MANAGER);

    Mockito.when(personService.findByToken(Mockito.anyString()))
        .thenReturn(personManager);

    Mockito.when(personService.updateRole("ADMIN", 1L))
        .thenThrow(new PersonNotFoundException());

    mockMvc.perform(MockMvcRequestBuilders.put("/clients/1")
            .header(HttpHeaders.AUTHORIZATION, "Bearer tokenmanager")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\n" +
                "  \"role\": \"ADMIN\"\n" +
                "}"))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.content().string("Usuario não encontrado!"));

    Mockito.verify(personService).updateRole("ADMIN", 1L);
  }

  @Test
  @DisplayName("Teste requisição sem o header PUT /clients/{id}")
  public void testPutHeaderNotFound() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.put("/clients/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\n" +
            "  \"role\": \"ADMIN\"\n" +
            "}"))
        .andExpect(MockMvcResultMatchers.status().isForbidden());
  }

  @Test
  @DisplayName("Teste requisição sem o token PUT /clients/{id}")
  public void testPutTokenNotFound() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.put("/clients/1")
            .header(HttpHeaders.AUTHORIZATION, "Bearer ")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\n" +
                "  \"role\": \"ADMIN\"\n" +
                "}"))
        .andExpect(MockMvcResultMatchers.status().isForbidden());
  }

  @Test
  @DisplayName("Teste requisição com usuario PUT /clients/{id}")
  public void testPutUser() throws Exception {
    Person personUser = new Person();
    personUser.setId(1L);
    personUser.setName("Xicrinho");
    personUser.setRole(Role.USER);

    Mockito.when(personService.findByToken(Mockito.anyString()))
            .thenReturn(personUser);

    mockMvc.perform(MockMvcRequestBuilders.put("/clients/1")
            .header(HttpHeaders.AUTHORIZATION, "Bearer tokenuser")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\n" +
                "  \"role\": \"ADMIN\"\n" +
                "}"))
        .andExpect(MockMvcResultMatchers.status().isForbidden());

    Mockito.verify(personService).findByToken(Mockito.anyString());
  }

  @Test
  @DisplayName("Teste requisição com admin PUT /clients/{id}")
  public void testPutAdmin() throws Exception {
    Person personAdmin = new Person();
    personAdmin.setId(1L);
    personAdmin.setName("Xicrinho");
    personAdmin.setRole(Role.USER);

    Mockito.when(personService.findByToken(Mockito.anyString()))
        .thenReturn(personAdmin);

    mockMvc.perform(MockMvcRequestBuilders.put("/clients/1")
            .header(HttpHeaders.AUTHORIZATION, "Bearer tokenadmin")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\n" +
                "  \"role\": \"ADMIN\"\n" +
                "}"))
        .andExpect(MockMvcResultMatchers.status().isForbidden());

    Mockito.verify(personService).findByToken(Mockito.anyString());
  }

  @Test
  @DisplayName("Teste requisição DELETE /clients/{id}")
  public void testDelete() throws Exception
  {
    Person personManager = new Person();
    personManager.setId(1L);
    personManager.setName("Xicrinho");
    personManager.setRole(Role.MANAGER);

    Mockito.when(personService.findByToken(Mockito.anyString()))
            .thenReturn(personManager);

    Mockito.when(personService.delete(1L))
        .thenReturn("Suceses!");

    mockMvc.perform(MockMvcRequestBuilders.delete("/clients/1")
            .header(HttpHeaders.AUTHORIZATION,"Bearer tokenmanager"))
        .andExpect(MockMvcResultMatchers.status().isNoContent());

    Mockito.verify(personService).delete(1L);
    Mockito.verify(personService).findByToken(Mockito.anyString());
  }

  @Test
  @DisplayName("Teste requisição caso usuario não seja encontrado DELETE /clients/{id}")
  public void testDeleteUserNotFound() throws Exception
  {
    Mockito.when(personService.findByToken(Mockito.anyString()))
            .thenThrow(new UsernameNotFoundException("Usuario não encontrado!"));

    mockMvc.perform(MockMvcRequestBuilders.delete("/clients/1")
        .header(HttpHeaders.AUTHORIZATION,"Bearer tokenmanager"))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.content().string("Usuario não encontrado!"));

    Mockito.verify(personService).findByToken(Mockito.anyString());
  }

  @Test
  @DisplayName("Teste requisição caso USER DELETE /clients/{id}")
  public void testUserDelete() throws Exception {
    Person personUser = new Person();
    personUser.setId(1L);
    personUser.setName("Xicrinho");
    personUser.setRole(Role.USER);

    Mockito.when(personService.findByToken(Mockito.anyString()))
        .thenReturn(personUser);

    mockMvc.perform(MockMvcRequestBuilders.delete("/clients/1")
        .header(HttpHeaders.AUTHORIZATION, "Bearer tokenuser"))
        .andExpect(MockMvcResultMatchers.status().isForbidden());
  }

  @Test
  @DisplayName("Teste requisição caso não seja passado o header DELETE /clients/{id}")
  public void testDeleteHeaderNotFound() throws Exception
  {
    mockMvc.perform(MockMvcRequestBuilders.delete("/clients/1"))
        .andExpect(MockMvcResultMatchers.status().isForbidden());
  }
}
