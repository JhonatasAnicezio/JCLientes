package com.api.clientes.util.exception;

public class PersonNotFoundException extends RuntimeException
{
  public PersonNotFoundException()
  {
    super("Usuario não encontrado!");
  }
}
