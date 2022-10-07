package com.example.hotelsservice.service;

import com.example.commondto.common.AddressDto;
import javax.persistence.EntityNotFoundException;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Primary
@FeignClient(name = "address-service")
@Headers({"some: {someToken}"})
public interface AddressFeignClient {

  String addressUri = "api/v1/addresses";

  @GetMapping(addressUri + "/{id}")
  AddressDto findById(@PathVariable final Integer id);

}


