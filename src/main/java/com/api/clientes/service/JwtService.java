package com.api.clientes.service;

import com.api.clientes.controller.dto.Token;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
  private final Algorithm algorithm;

  public JwtService(@Value("${jwt.secret}") String secret) {
    this.algorithm = Algorithm.HMAC256(secret);
  }

  public Token generateToken(String subject) {
    String encode = JWT.create().withSubject(subject).sign(algorithm);
    return new Token(encode);
  }

  public String getSubject(String token) {
    return JWT.require(algorithm).build().verify(token).getSubject();
  }
}
