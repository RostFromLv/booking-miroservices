package com.example.commondto.common;

import com.example.bookingcommonabstractservice.converter.Convertible;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@With
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentData implements Convertible {

  @NotNull(message = "Card number is null")
  private String cardNumber;

  @NotNull(message = "Price is null")
  private Double price;
}
