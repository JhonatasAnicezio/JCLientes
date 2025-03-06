package com.api.clientes.filter;

import com.api.clientes.model.entity.Person;
import com.api.clientes.service.PersonService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtFilter extends OncePerRequestFilter
{
  private final PersonService personService;

  public JwtFilter(PersonService personService)
  {
    this.personService = personService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException
  {
    String token = extractToken(request);

    if (token == null) {
      filterChain.doFilter(request, response);
      return;
    };

    Person person = personService.findByToken(token);

    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(person, null, person.getAuthorities());

    SecurityContextHolder.getContext().setAuthentication(authentication);

    filterChain.doFilter(request, response);
  }

  private String extractToken(HttpServletRequest request)
  {
    Optional<String> token = Optional.ofNullable(request.getHeader("Authorization"));

    if (token.isEmpty()) {
      return null;
    }

    return token.get().replace("Bearer ", "");
  }
}
