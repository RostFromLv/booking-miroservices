package com.example.commondto.common;

import com.example.bookingcommonabstractservice.converter.Convertible;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Collection;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

/**
 * Data transfer object for Order entity.
 *
 * @author Rostyslav
 * @since 1.0-SNAPSHOT
 */
@With
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDto implements Convertible {

  @NotNull(groups = Groups.Update.class, message = "Id cannot be null")
  private Integer id;

  @NotNull(message = "User id cannot be null")
  private Integer userId;

  @NotNull(message = "Hotel cannot be null")
  private Integer hotelId;

  @NotNull(message = "Hotel room id cannot be null")
  private Integer hotelRoomId;

  @NotNull(message = "Date from cannot be null")
  private Long fromDate;

  @NotNull(message = "End date from cannot be null")
  private Long endDate;

  @NotNull(message = "Status cannot be null")
  private Status status;

  @NotNull(message = "Create date cannot be null")
  private Long createdAt;

  @NotNull(message = "Expired date cannot be null")
  private Long expiredAt;

  @NotNull
  private CardDto card;

  private Collection<String> errors;

}
