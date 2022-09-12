package com.example.com.booking;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Use this class to customize Validation errors representing.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ValidationError extends Error {
  private Error error;
  private List<ValidationFieldError> validationError;
}
