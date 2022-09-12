package com.example.hotelsservice.service;

import com.example.commondto.common.AddressDto;
import com.example.commondto.common.Groups;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "address-service")
public interface AddressFeignClient {

  String addressUri = "api/v1/addresses";

  @GetMapping(addressUri+"/{id}")
  AddressDto getById(@PathVariable final Integer id);

  @PostMapping(addressUri)
  AddressDto create(@RequestBody @Validated(value = Groups.Create.class) AddressDto address);

}
