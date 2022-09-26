package com.example.ordersservice.service;

import com.example.commondto.common.PaymentRequest;
import com.example.commondto.common.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashSet;
import java.util.UUID;

@Primary
@FeignClient(name = "payments-service")
public interface PaymentFeignClient {

  @PostMapping("/api/v1/payments")
  PaymentResponse doPayment(@RequestBody PaymentRequest request);
}

