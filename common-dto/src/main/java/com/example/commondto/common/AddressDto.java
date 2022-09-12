package com.example.commondto.common;

import com.example.bookingcommonabstractservice.converter.Convertible;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * Data transfer object class for {@link com.booking.domain.Address} entity.
 *<p></p>
 * Use this DTO class for  receive and transfer information about {@link com.booking.domain.Address}
 * Also this DTO class have groups for create,update
 *
 * @author Rostyslav Balushchak
 * @since 1.0.0-SNAPSHOT
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressDto implements Convertible {
  @NotNull(groups = {Groups.Update.class},
        message = "Shouldn`t be null")
  @Min(value = 1, groups = {Groups.Create.class, Groups.Update.class},
        message = "Id should be more than 0")
  private Integer id;
  @NotBlank(groups = {Groups.Create.class, Groups.Update.class},
        message = "Shouldn`t  be blank")
  private String country;

  @NotBlank(groups = {Groups.Create.class, Groups.Update.class},
        message = "Shouldn`t  be blank")
  private String city;

  @NotBlank(groups = {Groups.Create.class, Groups.Update.class},
        message = "Shouldn`t  be blank")
  private String street;

  @NotNull(groups = {Groups.Update.class, Groups.Create.class},
        message = "Shouldn`t be null")
  @Min(value = 1, groups = {Groups.Create.class, Groups.Update.class},
        message = "houseNumber should be more than 0")
  private Integer houseNumber;

  @NotBlank(groups = {Groups.Create.class, Groups.Update.class},
        message = "Shouldn`t  be blank")
  private String postalCode;
}
