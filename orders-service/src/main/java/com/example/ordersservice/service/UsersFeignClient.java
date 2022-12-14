package com.example.ordersservice.service;

import com.example.commondto.common.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Primary
@FeignClient(name = "users-service")
public interface UsersFeignClient {

  String addressUri = "/api/v1/users";

  @GetMapping(addressUri + "/{id}")
  Optional<UserDto> getById(@PathVariable Integer id);

}