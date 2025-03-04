package com.api.clientes.service;

import com.api.clientes.model.entity.Person;
import com.api.clientes.model.repository.PersonRepository;
import com.api.clientes.util.Role;
import com.api.clientes.util.exception.PersonNotFoundException;
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
  private final JwtService jwtService;

  /**
   *  Constructor Person Service.
   *
   * @param personRepository Person Repository
   */
  public PersonService(
      PersonRepository personRepository,
      JwtService jwtService) {
    this.personRepository = personRepository;
    this.jwtService = jwtService;
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

  public Person findByToken(String token) throws UsernameNotFoundException
  {
    String subject = jwtService.getSubject(token);

    return personRepository.findByEmail(subject)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + subject));
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
  {
    return personRepository.findByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
  }

  public String delete(Long id)  {
    return personRepository.findById(id)
        .map(person -> {
          personRepository.delete(person);
          return "Person deleted successfully";
        }).orElseThrow(PersonNotFoundException::new);
  }

  public Person updateRole(Role role, Long id)
  {
    Person person = personRepository.findById(id)
        .orElseThrow(PersonNotFoundException::new);

    person.setRole(role);

    return personRepository.save(person);
  }
}
