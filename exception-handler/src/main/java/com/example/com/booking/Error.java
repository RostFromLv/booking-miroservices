package com.example.com.booking;

import java.time.LocalDateTime;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.Data;

/**
 * Use this class to customize error message.
 */
@Data
public class Error {
  @Min(400)
  @Max(600)
  private Integer code;
  private String message;
  private LocalDateTime timestamp;
}
