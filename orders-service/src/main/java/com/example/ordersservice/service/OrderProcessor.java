package com.example.ordersservice.service;


import com.example.commondto.common.OrderDto;

public interface OrderProcessor {

  OrderDto process(OrderDto order);

}
