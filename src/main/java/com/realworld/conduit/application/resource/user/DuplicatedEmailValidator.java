package com.realworld.conduit.application.resource.user;

import com.realworld.conduit.domain.repository.UserRepository;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class DuplicatedEmailValidator implements ConstraintValidator<DuplicatedEmailConstraint, String> {

  @Autowired
  private UserRepository userRepository;

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return (value == null || value.isEmpty()) || userRepository.findByEmail(value) == null;
  }
}
