package com.api.clientes.service;

import com.api.clientes.model.entity.Person;
import com.api.clientes.model.repository.PersonRepository;
import org.springframework.stereotype.Service;

/**
 *  Class Person Service.
 */
@Service
public class PersonService {
  private final PersonRepository personRepository;

  /**
   *  Constructor Person Service.
   *
   * @param personRepository Person Repository
   */
  public PersonService(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  /**
   *  Method for create Person.
   *
   * @param person person to be created
   * @return return person saved
   */
  public Person create(Person person) {
    return personRepository.save(person);
  }
}
