package com.example.com.booking;

import lombok.Data;

/**
 * Use this class for customize Validation error.
 */
@Data
public class ValidationFieldError {
  private String field;
  private String error;
}
