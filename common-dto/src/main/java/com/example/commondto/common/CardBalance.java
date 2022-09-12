package com.example.commondto.common;

import com.example.bookingcommonabstractservice.converter.Convertible;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@With
@AllArgsConstructor
@NoArgsConstructor
public class CardBalance implements Serializable, Convertible {

  private static final long serialVersionUID = -6152394706902303052L;

  @NotNull(message = "Card number can`t be null")
  private String cardNumber;
  @NotNull(message = "Card balance can`t be null")
  private Double balance;
}
