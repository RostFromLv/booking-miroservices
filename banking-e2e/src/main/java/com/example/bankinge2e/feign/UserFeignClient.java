package com.example.bankinge2e.feign;

import com.example.commondto.common.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "users-service", url = "localhost:8082/api/v1/users")
public interface UserFeignClient {

  @GetMapping("/{id}")
  UserDto getById(@PathVariable Integer id);

  @PostMapping
  UserDto create(@RequestBody UserDto userDto);

  @DeleteMapping
  void deleteAllUsers();
}
