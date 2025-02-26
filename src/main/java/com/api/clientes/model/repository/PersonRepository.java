package com.api.clientes.model.repository;

import com.api.clientes.model.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *  Person Repository class.
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
}
