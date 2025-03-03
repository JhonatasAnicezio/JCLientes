package com.api.clientes.controller.advice;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 *  Controller Advice for Errors.
 */
@ControllerAdvice
public class GlobalControllerAdvice {

  /**
   *  Method for handling errors.
   *
   * @param exception exception error
   * @return return Response Map Error
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleNotValidate(MethodArgumentNotValidException exception) {
    Map<String, String> errors = new HashMap<>();

    for(FieldError error : exception.getBindingResult().getFieldErrors()) {
      errors.put(error.getField(), error.getDefaultMessage());
    }
    
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(errors);
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  public ResponseEntity<String> handleUsernameNotFound(
      UsernameNotFoundException exception)
  {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(exception.getMessage());
  }
}
