package com.example.commondto.common;

import com.example.bookingcommonabstractservice.converter.Convertible;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
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
public class RoomPriceDto implements Convertible {

  @NotNull(message = "Room price id cannot be null", groups = Groups.Create.class)
  private Integer id;
  @NotNull(message = "Default price cannot be null")
  private Double defaultPrice;
  @NotNull(message = "Price cannot be null")
  private Double price;
  @NotNull(message = "Start date cannot be null", groups = Groups.Create.class)
  @Null(message = "Start date should be null for update",groups = Groups.Update.class)
  private Long startDate;
  @NotNull(message = "End date cannot be null", groups = Groups.Create.class)
  @Null(message = "End date should be null for update",groups = Groups.Update.class)
  private Long endDate;
}