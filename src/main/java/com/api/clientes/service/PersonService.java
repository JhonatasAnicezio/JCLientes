package com.api.clientes.service;

import com.api.clientes.model.entity.Person;
import com.api.clientes.model.repository.PersonRepository;
import org.springframework.stereotype.Service;

@Service
public class PersonService {
  private final PersonRepository personRepository;

  public PersonService(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  public Person create(Person person) {
    return personRepository.save(person);
  }
}
