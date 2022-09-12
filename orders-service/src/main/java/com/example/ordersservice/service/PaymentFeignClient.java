package com.example.ordersservice.service;

import com.example.commondto.common.PaymentRequest;
import com.example.commondto.common.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payments-service")
public interface PaymentFeignClient {

  @PostMapping("/api/v1/payments")
  PaymentResponse doPayment(@RequestBody PaymentRequest request);
}
