package com.api.clientes.filter;

import com.api.clientes.model.entity.Person;
import com.api.clientes.service.PersonService;
import com.auth0.jwt.exceptions.JWTDecodeException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    try
    {
      String token = extractToken(request);

      if (token == null || token.isBlank()) {
        filterChain.doFilter(request, response);
        return;
      };

      Person person = personService.findByToken(token);

      request.setAttribute("authenticatedUser", person);

      UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(person, null, person.getAuthorities());

      SecurityContextHolder.getContext().setAuthentication(authentication);

      filterChain.doFilter(request, response);
    } catch (JWTDecodeException e) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json");
      response.getWriter().write(e.getMessage());
    } catch (UsernameNotFoundException e) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      response.setContentType("application/json");
      response.getWriter().write(e.getMessage());
    }
  }

  private String extractToken(HttpServletRequest request)
  {
    Optional<String> token = Optional.ofNullable(request.getHeader("Authorization"));

    return token.map(s -> s.replace("Bearer ", "")).orElse(null);
  }
}
