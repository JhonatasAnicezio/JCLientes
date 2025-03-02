package com.api.clientes.service;

import com.api.clientes.model.entity.Person;
import com.api.clientes.model.repository.PersonRepository;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *  Class Person Service.
 */
@Service
public class PersonService implements UserDetailsService
{
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
    String hashedPassword = new BCryptPasswordEncoder().encode(person.getPassword());

    person.setPassword(hashedPassword);

    return personRepository.save(person);
  }

  public List<Person> findAll() {
    return personRepository.findAll();
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
  {
    return personRepository.findByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
  }
}
