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
@FeignClient(name = "hotels-service",fallback = HotelFallback.class)
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

@Component
class HotelFallback implements HotelFeignClient{

  @Override
  public ReservationDto createReservation(ReservationDto reservationDto) {
    throw new UnsupportedOperationException(String.format("Cannot create reservation: %s .Check your data.Check available to reserve for picked dates",reservationDto));
  }

  @Override
  public void deleteReservation(Integer id) {
    throw new UnsupportedOperationException(String.format("Cannot delete reservation with id : %s . Check your data.",id));
  }

  @Override
  public HotelRoomDto getRoomById(Integer roomId) {
    throw new EntityNotFoundException(String.format("Cannot find room with id: %s",roomId));
  }

  @Override
  public RoomPriceDto getPrice(Integer id) {
    RoomPriceDto roomPriceDto = new RoomPriceDto();
    roomPriceDto.setPrice(500d);//for example the lowest price for any room
                                // Also can create the script with default price for each type of room and assign default price to ever
    roomPriceDto.setDefaultPrice(450d);
    roomPriceDto.setId(0);
    roomPriceDto.setStartDate(null);
    roomPriceDto.setEndDate(null);
    return roomPriceDto;
  }
}