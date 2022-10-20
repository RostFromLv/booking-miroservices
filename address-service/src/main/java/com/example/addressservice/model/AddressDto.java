package com.example.addressservice.model;

import com.example.bookingcommonabstractservice.converter.Convertible;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressDto implements Convertible {

  private
  @NotNull(groups = {Groups.Update.class}, message = "Shouldn`t be null")
  @Min(value = 1, groups = {Groups.Create.class,
      Groups.Update.class}, message = "Id should be more than 0")
  Integer id;

  private
  @NotBlank(groups = {Groups.Create.class, Groups.Update.class},
      message = "Shouldn`t  be blank")
  String country;

  private
  @NotBlank(groups = {Groups.Create.class, Groups.Update.class},
      message = "Shouldn`t  be blank")
  String city;

  private
  @NotBlank(groups = {Groups.Create.class, Groups.Update.class},
      message = "Shouldn`t  be blank")
  String street;

  private
  @NotNull(groups = {Groups.Update.class, Groups.Create.class},
      message = "Shouldn`t be null")
  @Min(value = 1, groups = {Groups.Create.class, Groups.Update.class},
      message = "houseNumber should be more than 0")
  Integer houseNumber;

  private
  @NotBlank(groups = {Groups.Create.class, Groups.Update.class},
      message = "Shouldn`t  be blank")
  String postalCode;
}
