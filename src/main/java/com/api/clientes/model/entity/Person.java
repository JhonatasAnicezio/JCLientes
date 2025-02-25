package com.api.clientes.model.entity;

import com.api.clientes.util.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "persons")
public class Person {
  @jakarta.persistence.Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long Id;

  private String name;
  private String email;
  private String password;
  private Role role;

  public Person(
      String name,
      String email,
      String password,
      Role role) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.role = role;
  }
}
