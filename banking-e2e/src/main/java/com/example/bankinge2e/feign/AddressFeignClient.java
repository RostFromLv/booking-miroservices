package com.example.bankinge2e.feign;

import com.example.commondto.common.AddressDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "address-service",url = "localhost:8081/api/v1/addresses")
public interface AddressFeignClient {

  @PostMapping
  AddressDto create(@RequestBody AddressDto address);

  @DeleteMapping("/{id}")
  void delete(@PathVariable final Integer id);

  @DeleteMapping
  void deleteAll();

}
