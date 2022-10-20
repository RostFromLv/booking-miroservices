package com.example.ordersservice;

import com.example.commondto.common.OrderDto;
import com.example.commondto.common.ReservationDto;
import com.example.commondto.common.Status;
import com.example.commondto.common.UserDto;
import com.example.ordersservice.service.HotelFeignClient;
import com.example.ordersservice.service.OrderProcessorSaga;
import com.example.ordersservice.service.OrderService;
import com.example.ordersservice.service.UsersFeignClient;
import java.util.Optional;
import javax.persistence.EntityExistsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("initialization.field.uninitialized")
public class OrderProcessorSagaUT {

  @Mock
  HotelFeignClient hotelFeignClient;
  @Mock
  UsersFeignClient usersFeignClient;
  @Mock
  OrderService orderService;

  @InjectMocks
  OrderProcessorSaga orderProcessorSaga;

  private OrderDto orderDto = new OrderDto();
  private UserDto userDto = new UserDto();
  private ReservationDto reservationDto = new ReservationDto();

  private final Integer orderId = 1;
  private final Status orderStatus = Status.OPEN;
  private final Integer hotelId = 1;
  private final Integer roomId = 1;
  private final Integer userId = 1;
  private final Long fromDate = 100L;
  private final Long endDate = 200l;

  @BeforeEach
  void beforeEach() {
    orderDto = new OrderDto()
          .withId(orderId)
          .withStatus(orderStatus)
          .withHotelId(hotelId)
          .withHotelRoomId(roomId)
          .withUserId(userId)
          .withFromDate(fromDate)
          .withEndDate(endDate);
    userDto.setId(1);

    reservationDto.setDateFrom(fromDate);
    reservationDto.setHotelId(hotelId);
    reservationDto.setRoomId(roomId);
    reservationDto.setDateTo(endDate);
  }


  @Test
  void processWithCorrectData_ShouldReturn_createdOrder() {
    Mockito.when(usersFeignClient.getById(orderDto.getId())).thenReturn(Optional.ofNullable(userDto));

    Mockito.when(hotelFeignClient.createReservation(reservationDto)).thenReturn(reservationDto.withId(1));

    Mockito.when(orderService.create(orderDto)).thenReturn(orderDto);
    Mockito.when(orderService.update(orderDto.withStatus(Status.PENDING), orderDto.getId())).thenReturn(orderDto.withStatus(Status.PENDING));
    Mockito.when(orderService.update(orderDto.withStatus(Status.ACCEPT), orderDto.getId())).thenReturn(orderDto.withStatus(Status.ACCEPT));

    OrderDto actual = orderProcessorSaga.process(orderDto);
    Assertions.assertEquals(orderDto.withStatus(Status.ACCEPT), actual);
  }

  @Test
  void processWithWrongUserId_ShouldThrow_EntityExistException() {

    orderDto.setUserId(5);
    Mockito.when(orderService.create(orderDto.withStatus(Status.OPEN))).thenReturn(orderDto.withStatus(Status.OPEN));
    Mockito.when(usersFeignClient.getById(orderDto.getUserId())).thenThrow(EntityExistsException.class);
    Assertions.assertThrows(EntityExistsException.class, () -> orderProcessorSaga.process(orderDto));
  }

  @Test
  void processWithWrongRoomDateReservation_ShouldThrow_IllegalArgumentException() {

    Mockito.when(orderService.create(orderDto)).thenReturn(orderDto);
    Mockito.when(usersFeignClient.getById(orderDto.getUserId())).thenReturn(Optional.ofNullable(userDto));
    Mockito.when(hotelFeignClient.createReservation(reservationDto)).thenThrow(IllegalArgumentException.class);
    Assertions.assertThrows(IllegalArgumentException.class, () -> orderProcessorSaga.process(orderDto));
  }

}
