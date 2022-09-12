package com.example.commondto.common;

import com.example.bookingcommonabstractservice.converter.Convertible;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Convert;
import lombok.Data;

@Data
public class PaymentResponse implements Convertible {
  private UUID transactionId;
  private Long timestamp;
  private Boolean success;
  private Set<String> errors = new HashSet<>();

  public final boolean hasError() {
    return !errors.isEmpty();
  }

  public final void addError(String message) {
    errors.add(message);
  }

}
