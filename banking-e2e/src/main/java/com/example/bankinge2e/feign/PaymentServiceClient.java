package com.example.bankinge2e.feign;

import com.example.commondto.common.CardBalance;
import com.example.commondto.common.PaymentData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payments-service", url = "localhost:8085/api/v1/payments")
public interface PaymentServiceClient {
  String cardUri = "/card";

  @GetMapping(cardUri + "/{cardNumber}")
  CardBalance findByNumber(@PathVariable String cardNumber);

  @PostMapping(cardUri)
  void createCard(@RequestBody CardBalance cardBalance);

  @PutMapping("/card/replenish")
  void replenish(@RequestBody PaymentData data );

  @DeleteMapping(cardUri + "/{cardNumber}")
  void delete(@PathVariable String cardNumber);


}
