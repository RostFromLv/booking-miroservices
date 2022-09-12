package com.example.bankinge2e.feign;

import com.example.commondto.common.HotelDto;
import com.example.commondto.common.HotelRoomDto;
import com.example.commondto.common.RoomPriceDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "hotels-service", url = "localhost:8083/api/v1/hotels")
public interface HotelFeignClient {

  String roomsUrl = "/rooms";
  String pricesUrl = "/prices";
  String reservationsUrl = "/reservations";

  //Hotels
  @PostMapping
  HotelDto createHotel(@RequestBody HotelDto creatingDto);

  @GetMapping("/{id}")
  HotelDto getHotelById(@PathVariable Integer id);

  @DeleteMapping
  void deleteAllHotels();

  //Rooms
  @PostMapping(roomsUrl)
  HotelRoomDto createRoom(@RequestBody HotelRoomDto createHotelRoomDto);

  @PutMapping(roomsUrl)
  HotelRoomDto updateRoom(@RequestBody HotelRoomDto updateHotelRoomDto);

  @GetMapping("/rooms/{roomId}")
  HotelRoomDto getRoomById(@PathVariable Integer roomId);

  @DeleteMapping(roomsUrl)
  void deleteAllRooms();


  //Prices
  @PostMapping(pricesUrl)
  RoomPriceDto createRoomPrice(@RequestBody RoomPriceDto roomPrice);

  @GetMapping(pricesUrl+"/{id}")
  RoomPriceDto getPrice(@PathVariable Integer id);

  @DeleteMapping(pricesUrl)
  void deleteAllPrices();

  @DeleteMapping(reservationsUrl)
  void deleteAllReservations();

}
