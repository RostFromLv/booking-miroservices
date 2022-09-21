package com.example.hotelsservice.service;

import com.example.commondto.common.AddressDto;
import javax.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Primary
@FeignClient(name = "address-service", fallback = AddressFallback.class)
public interface AddressFeignClient {

  String addressUri = "api/v1/addresses";

  @GetMapping(addressUri + "/{id}")
  AddressDto findById(@PathVariable final Integer id);

}

@Component
@Log4j
class AddressFallback implements AddressFeignClient {

  @Override
  public AddressDto findById(Integer id) {
    log.info(String.format("Triggered fallback in method findById(%s) to Address service.",id));
    throw new EntityNotFoundException("No hotel found with id : " + id);
  }

}
