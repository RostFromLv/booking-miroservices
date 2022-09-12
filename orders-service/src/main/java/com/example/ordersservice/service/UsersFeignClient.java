package com.example.ordersservice.service;

import com.example.commondto.common.UserDto;
import java.util.Optional;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "users-service")
public interface UsersFeignClient {

  String addressUri = "/api/v1/users";

  @GetMapping(addressUri+"/{id}")
  Optional<UserDto> getById(@PathVariable Integer id);

}
