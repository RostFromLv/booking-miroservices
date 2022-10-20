package com.example.commondto.common;

import com.example.bookingcommonabstractservice.converter.Convertible;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * Class for transport user entity.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("initialization.field.uninitialized")
public class UserDto implements Convertible {

  @Min(value = 1, message = "Id cannot be less than 0")
  @NotNull(groups = {Groups.Update.class}, message = "Id cannot be null")
  private Integer id;

  @NotBlank(groups = {Groups.Create.class, Groups.Update.class}, message = "First name cannot be blank")
  private String firstName;

  @NotBlank(groups = {Groups.Create.class,
      Groups.Update.class}, message = "Last name cannot be blank")
  private String lastName;

  @Email
  @NotBlank(groups = {Groups.Create.class, Groups.Update.class},
      message = "Email cannot be blank")
  private String email;

  @NotBlank(groups = {Groups.Create.class,
      Groups.Update.class}, message = "Phone number cannot be blank")
  private String phoneNumber;

  private long createdAt;

  private long updatedAt;
}
