package com.api.clientes.util;

/**
 *  Class enum Role.
 */
public enum Role {
  ADMIN("ADMIN"),
  USER("USER"),
  MANAGER("MANAGER");

  private final String role;

  Role(String role) {
    this.role = role;
  }

  public String getRole() {
    return role;
  }
}
