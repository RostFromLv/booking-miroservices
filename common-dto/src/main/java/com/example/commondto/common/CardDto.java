package com.example.commondto.common;

import com.example.bookingcommonabstractservice.converter.Convertible;
import lombok.Data;

@Data
public class CardDto implements Convertible {
  private String number;
  private String ccv2;
  private Long expirationDate;
  private String holderFullName;
}
