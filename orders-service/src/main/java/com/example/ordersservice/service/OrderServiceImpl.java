package com.example.ordersservice.service;

import com.example.bookingcommonabstractservice.service.AbstractDataService;
import com.example.commondto.common.OrderDto;
import com.example.ordersservice.domain.Order;
import org.springframework.stereotype.Service;

/**
 * Implementation of  OrderService.
 */
@Service
public class OrderServiceImpl extends AbstractDataService<Integer, Order, OrderDto> implements OrderService {

}
