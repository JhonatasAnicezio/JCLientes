package com.api.clientes.controller;

import com.api.clientes.controller.dto.AuthDto;
import com.api.clientes.controller.dto.Token;
import com.api.clientes.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  public AuthenticationController(
      AuthenticationManager authenticationManager,
      JwtService jwtService) {
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
  }

  @PostMapping
  public ResponseEntity<Token> login(
      @Valid @RequestBody AuthDto authDto
  ) {
    UsernamePasswordAuthenticationToken usernamePassword =
        new UsernamePasswordAuthenticationToken(authDto.email(), authDto.password());

    Authentication user = authenticationManager.authenticate(usernamePassword);

    return ResponseEntity.status(HttpStatus.OK)
        .body(jwtService.generateToken(user.getName()));
  }
}
