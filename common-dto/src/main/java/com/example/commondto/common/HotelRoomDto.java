package com.example.commondto.common;

import com.example.bookingcommonabstractservice.converter.Convertible;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@With
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HotelRoomDto implements Convertible {
  @NotNull(message = "Id cannot be null", groups = Groups.Update.class)
  @Min(value = 1, message = "Id cannot be less than 1", groups = {Groups.Update.class})
  private Integer id;

  @NotBlank(message = "Type cannot be blank")
  private String type;

  @NotNull(message = "Area cannot be null")
  private Float area;

  @NotNull(message = "Hotel id cannot be null", groups = Groups.Create.class)
  @Null(message = "Hotel id should be null for update", groups = Groups.Update.class)
  private Integer hotelId;

  private Integer roomPriceId;
}
