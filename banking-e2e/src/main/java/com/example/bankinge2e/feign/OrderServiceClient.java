package com.example.bankinge2e.feign;

import com.example.commondto.common.OrderDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "orders-service", url = "localhost:8084/api/v1/orders")
public interface OrderServiceClient {

  @PostMapping("/payments")
  OrderDto doPayment(@RequestBody OrderDto orderDto);

  @DeleteMapping("/{id}")
  void removeOrder(@PathVariable Integer id);

  @DeleteMapping
  void deleteAllOrders();

}
