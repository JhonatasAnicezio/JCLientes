package com.api.clientes.service;

import com.api.clientes.controller.dto.Token;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Testes camada service: JwtService")
public class JwtServiceTest {
  @Autowired
  JwtService jwtService;

  @Test
  @DisplayName("Test method encode")
  public void testEncode() {
    String username = "Xicrinho";

    Token token = jwtService.generateToken(username);

    Assertions.assertNotNull(token);
    Assertions.assertNotEquals(username, token.token());
  }

  @Test
  @DisplayName("Test get subject")
  public void testGetSubject() {
    String username = "Xicrinho";
    Token token = jwtService.generateToken(username);

    Assertions.assertNotEquals(username, token.token());

    String subject = jwtService.getSubject(token.token());

    Assertions.assertEquals(username, subject);
  }
}
