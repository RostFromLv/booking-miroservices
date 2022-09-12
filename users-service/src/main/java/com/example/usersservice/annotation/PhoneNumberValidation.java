package com.example.usersservice.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Class with logic for hone number validation.
 */
public class PhoneNumberValidation implements ConstraintValidator<PhoneNumber, String> {
  private String validationRegEx = "";

  @Override
  public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
    return true;
  }
}
