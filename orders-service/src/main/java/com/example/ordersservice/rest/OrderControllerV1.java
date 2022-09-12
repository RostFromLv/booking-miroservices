package com.example.ordersservice.rest;

import com.example.commondto.common.Groups;
import com.example.commondto.common.OrderDto;
import com.example.ordersservice.service.OrderProcessor;
import com.example.ordersservice.service.OrderService;
import com.example.ordersservice.service.OrderServiceImpl;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * The controller class for call orders api.
 */
@RestController
@RequestMapping("/api/v1/orders")
public class OrderControllerV1 {

  private final OrderService orderService;
  private final OrderProcessor orderProcessor;

  @Autowired
  public OrderControllerV1(OrderServiceImpl orderService, OrderProcessor orderProcessor) {
    this.orderService = orderService;
    this.orderProcessor = orderProcessor;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  OrderDto create(@RequestBody @Validated(value = Groups.Create.class) OrderDto createDto) {
    return orderService.create(createDto);
  }

  @PostMapping("/payments")
  @ResponseStatus(HttpStatus.OK)
  OrderDto doPayment(@RequestBody OrderDto orderDto){
    return orderProcessor.process(orderDto);
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  OrderDto getById(@PathVariable Integer id) {
    return orderService.findById(id).get();
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  Collection<OrderDto> getAll() {
    return orderService.findAll();
  }

  @PutMapping
  @ResponseStatus(HttpStatus.OK)
  OrderDto update(@RequestBody OrderDto orderDto){
    return orderService.update(orderDto, orderDto.getId());
  }


  @DeleteMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteAllOrders(){
    orderService.deleteAll();
  }
}
