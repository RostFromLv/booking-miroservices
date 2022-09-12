package com.example.ordersservice.service;

import com.example.commondto.common.OrderDto;
import com.example.commondto.common.PaymentRequest;
import com.example.commondto.common.PaymentResponse;
import com.example.commondto.common.ReservationDto;
import com.example.commondto.common.Status;
import feign.FeignException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Collection;
import java.util.HashSet;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
public class OrderProcessorSaga implements OrderProcessor {

  private final HotelFeignClient hotelClient;
  private final UsersFeignClient usersClient;
  private final OrderService orderService;
  private final PaymentFeignClient paymentClient;

  @Autowired
  public OrderProcessorSaga(HotelFeignClient hotelClient, UsersFeignClient usersClient, OrderService orderService, PaymentFeignClient paymentClient) {
    this.hotelClient = hotelClient;
    this.usersClient = usersClient;
    this.orderService = orderService;
    this.paymentClient = paymentClient;
  }



  @Override
  @Transactional
  public OrderDto process(OrderDto order) {
    Assert.notNull(order);
    order.setStatus(Status.OPEN);

    OrderDto target = orderService.create(order).withCard(order.getCard());
    Collection<String> errors = new HashSet<>();

    target.setStatus(Status.PENDING);
    target = orderService.update(target, target.getId());

    ReservationDto reservation = null;

    try {
      usersClient.getById(order.getUserId());

      reservation = reserveHotelRoom(toReservation(order));

      Double defaultPricePerDay = hotelClient.getPrice(
            hotelClient.getRoomById(order.getHotelRoomId()).getRoomPriceId()).getPrice();

      Double finishPrice = calculateFinishPrice(defaultPricePerDay, getDayPeriod(order.getFromDate(), order.getEndDate()));

      PaymentRequest paymentRequest = new PaymentRequest();
      paymentRequest.setCardDto(order.getCard());
      paymentRequest.setPrice(finishPrice);

      PaymentResponse response = paymentClient.doPayment(paymentRequest);

      if (response.getSuccess().equals(true)) {
        target.setStatus(Status.ACCEPT);
      }else {
        target.setStatus(Status.REJECT);
      }
    } catch (FeignException e) {
      errors.add(e.getMessage());
      target.setStatus(Status.REJECT);
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
      target.setStatus(Status.REJECT);
    } finally {
      if (target.getStatus().equals(Status.REJECT)){
        deleteReservation(reservation);
      }
    }

    return orderService.update(target, target.getId()).withCard(order.getCard()).withErrors(errors);
  }

  private ReservationDto reserveHotelRoom(ReservationDto reservation) {
    return hotelClient.createReservation(reservation);
  }

  private Double calculateFinishPrice(Double pricePerDay, Integer days) {
    Assert.notNull(pricePerDay);
    Assert.notNull(days);
    double result = pricePerDay * days;
    Assert.isTrue(result > 0);
    return result;
  }

  ;

  private ReservationDto toReservation(OrderDto order) {
    return new ReservationDto()
          .withHotelId(order.getHotelId())
          .withRoomId(order.getHotelRoomId())
          .withDateFrom(order.getFromDate())
          .withDateTo(order.getEndDate());
  }

  private Integer getDayPeriod(Long startDay, Long endDay) {
    Assert.isTrue(startDay < endDay);

    LocalDate startDate = Instant.ofEpochMilli(startDay).atZone(ZoneId.systemDefault()).toLocalDate();
    LocalDate endDate = Instant.ofEpochMilli(endDay).atZone(ZoneId.systemDefault()).toLocalDate();
    int days = Period.between(startDate, endDate).getDays();
    Assert.isTrue(days > 0);
    return days;
  }
  public void deleteReservation(ReservationDto reservation){
    if (reservation !=null){
      hotelClient.deleteReservation(reservation.getId());
    }
  }
}
