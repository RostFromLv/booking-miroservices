package com.example.ordersservice.service;

import com.example.commondto.common.HotelRoomDto;
import com.example.commondto.common.ReservationDto;
import com.example.commondto.common.RoomPriceDto;
import javax.persistence.EntityNotFoundException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Primary
@FeignClient(name = "hotels-service")
public interface HotelFeignClient {

  String hotelUrl ="/api/v1/hotels";
  String reservationUri = hotelUrl+"/reservations";

  @PostMapping(reservationUri)
  ReservationDto createReservation(@RequestBody ReservationDto reservationDto);

  @DeleteMapping(reservationUri+"/{id}")
  void deleteReservation(@PathVariable Integer id);

  @GetMapping(hotelUrl+"/rooms/{roomId}")
  HotelRoomDto getRoomById(@PathVariable Integer roomId);

  @GetMapping(hotelUrl+"/prices/{id}")
  RoomPriceDto getPrice(@PathVariable Integer id);
}
