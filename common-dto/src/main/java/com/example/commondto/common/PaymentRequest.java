package com.example.commondto.common;

import com.example.bookingcommonabstractservice.converter.Convertible;
import lombok.Data;

@Data
public class PaymentRequest implements Convertible {
  private CardDto cardDto;
  private Double price;
}
