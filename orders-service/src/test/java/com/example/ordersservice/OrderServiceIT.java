package com.example.ordersservice;

import com.example.commondto.common.OrderDto;
import com.example.commondto.common.Status;
import com.example.ordersservice.service.OrderRepository;
import com.example.ordersservice.service.OrderServiceImpl;
import javax.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class OrderServiceIT {

  private final static Integer ID = 1;
  private final static Integer USER_ID = 2;
  private final static Integer HOTEL_ROOM_ID = 1;
  private final static Integer HOTEL_ID = 1;
  private final static Status STATUS = Status.OPEN;
  private final static Long FROM_DATE = 10L;
  private final static Long END_DATE = 1000L;
  private final static Long EXPIRED_AT = 9999999L;

  private final OrderServiceImpl orderService;
  private final OrderRepository orderRepository;

  @Autowired
  public OrderServiceIT(OrderServiceImpl orderService, OrderRepository orderRepository) {
    this.orderService = orderService;
    this.orderRepository = orderRepository;
  }
  @BeforeEach
  void beforeEach(){
    orderRepository.deleteAll();
  }

  //Create
  @Test
  void createByCorrectDto_ShouldReturn_CreatedOrderDto() {
    OrderDto orderDtoForCreate = generateOrderDto();
    OrderDto actual = orderService.create(orderDtoForCreate);
    Assert.notNull(actual);
    Assertions.assertThat(actual)
          .usingRecursiveComparison()
          .ignoringFields("id","createdAt")
          .isEqualTo(orderDtoForCreate);
  }

  //Get by id
  @Test
  void getByExistId_ShouldReturn_ExistOrderDto() {
    OrderDto existOrderDto = orderService.create(generateOrderDto());
    OrderDto actual = orderService.findById(existOrderDto.getId()).get();
    org.junit.jupiter.api.Assertions.assertEquals(existOrderDto,actual);
  }

  @Test
  void getByWrongId_ShouldThrow_EntityNotFoundException() {
    org.junit.jupiter.api.Assertions.assertThrows(EntityNotFoundException.class,()-> orderService.findById(ID).get());
  }

  //Get all
  @Test
  void getAll_ShouldReturn_FullList() {
    orderService.create(generateOrderDto());
    orderService.create(generateOrderDto());
    org.junit.jupiter.api.Assertions.assertEquals(2,orderService.findAll().size());
  }

  @Test
  void getAll_ShouldReturn_EmptyList() {
    org.junit.jupiter.api.Assertions.assertEquals(0,orderService.findAll().size());
  }

  private OrderDto generateOrderDto() {
    OrderDto orderDto = new OrderDto();
    orderDto.setUserId(USER_ID);
    orderDto.setHotelRoomId(HOTEL_ROOM_ID);
    orderDto.setHotelId(HOTEL_ID);
    orderDto.setStatus(STATUS);
    orderDto.setFromDate(FROM_DATE);
    orderDto.setEndDate(END_DATE);
    orderDto.setExpiredAt(EXPIRED_AT);
    return orderDto;
  }
}
