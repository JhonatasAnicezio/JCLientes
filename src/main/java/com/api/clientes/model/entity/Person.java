package com.api.clientes.model.entity;

import com.api.clientes.util.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  Class Entity Person.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "persons")
public class Person {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String email;
  private String password;
  private Role role;

  /**
   *  Constructor Person for CreationDto.
   *
   * @param name person's name
   * @param email person's email
   * @param password person's password
   * @param role person's role
   */
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
