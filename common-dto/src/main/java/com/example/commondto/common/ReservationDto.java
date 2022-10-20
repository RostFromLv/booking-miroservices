package com.example.commondto.common;

import com.example.bookingcommonabstractservice.converter.Convertible;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@With
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("initialization.field.uninitialized")
public class ReservationDto implements Convertible {
  @NotNull
  private Integer id;
  @NotNull
  private Integer hotelId;
  @NotNull
  private Integer roomId;
  @NotNull
  private Long dateFrom;
  @NotNull
  private Long dateTo;
}
