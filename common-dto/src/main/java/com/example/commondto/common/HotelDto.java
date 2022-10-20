package com.example.commondto.common;

import com.example.bookingcommonabstractservice.converter.Convertible;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

/**
 * Use this dto to transfer information about Hotels.
 */
@Data
@With
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HotelDto implements Convertible {


  @NotNull(message = "Id cannot be null", groups = Groups.Update.class)
  @Min(value = 1, message = "Id cannot be less than 1", groups = {Groups.Create.class})
  private Integer id;

  @NotNull(message = "Name cannot be blank")
  private String name;

  @NotNull(message = "Address id cannot be null")
  private Integer addressId;
}
