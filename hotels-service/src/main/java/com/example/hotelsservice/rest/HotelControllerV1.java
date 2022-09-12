package com.example.hotelsservice.rest;

import com.example.commondto.common.Groups;
import com.example.commondto.common.HotelDto;
import com.example.commondto.common.HotelRoomDto;
import com.example.commondto.common.RoomPriceDto;
import com.example.hotelsservice.service.HotelRoomReservationService;
import com.example.hotelsservice.service.HotelRoomService;
import com.example.hotelsservice.service.HotelRoomsServiceImpl;
import com.example.hotelsservice.service.HotelService;
import com.example.hotelsservice.service.HotelServiceImpl;
import com.example.hotelsservice.service.RoomPriceService;
import java.util.Collection;
import java.util.List;
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
 * The class that processes request to Hotel and Room.
 */
@RestController
@RequestMapping("/api/v1/hotels")
public class HotelControllerV1 {

  private final HotelService hotelService;
  private final HotelRoomService hotelRoomsService;
  private final RoomPriceService roomPriceService;

  private final String roomsUrl = "/rooms";
  private final String pricesUrl = "/prices";

  @Autowired
  public HotelControllerV1(HotelServiceImpl hotelService, HotelRoomsServiceImpl hotelRoomsService, HotelRoomReservationService reservationService, RoomPriceService roomPriceService) {
    this.hotelService = hotelService;
    this.hotelRoomsService = hotelRoomsService;
    this.roomPriceService = roomPriceService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public HotelDto createHotel(@RequestBody @Validated(value = Groups.Create.class) HotelDto creatingDto) {
    return hotelService.create(creatingDto);
  }

  @PutMapping
  @ResponseStatus(HttpStatus.OK)
  public HotelDto updateHotel(@RequestBody @Validated(value = Groups.Update.class) HotelDto updatingDto) {
    return hotelService.update(updatingDto, updatingDto.getId());
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Collection<HotelDto> getAllHotels() {
    return hotelService.findAll();
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public HotelDto getHotelById(@PathVariable Integer id) {
    return hotelService.findById(id).get();
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteHotel(@PathVariable Integer id) {
    hotelService.deleteById(id);
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteAllHotels(){
    hotelService.deleteAll();
  }

  // ROOM endpoints

  @PostMapping(roomsUrl)
  @ResponseStatus(HttpStatus.CREATED)
  public HotelRoomDto createByHotel(@RequestBody @Validated(value = Groups.Create.class) HotelRoomDto createHotelRoomDto) {
    return hotelRoomsService.create(createHotelRoomDto);
  }

  @PutMapping(roomsUrl)
  @ResponseStatus(HttpStatus.OK)
  public HotelRoomDto updateByHotel(@RequestBody @Validated(value = Groups.Update.class) HotelRoomDto updateHotelRoomDto) {
    return hotelRoomsService.update(updateHotelRoomDto, updateHotelRoomDto.getId());
  }

  @GetMapping("/{hotelId}"+roomsUrl)
  @ResponseStatus(HttpStatus.OK)
  public List<HotelRoomDto> getAllRoomsByHotel(@PathVariable Integer hotelId) {
    return hotelRoomsService.getByHotelId(hotelId);
  }
  @GetMapping(roomsUrl)
  @ResponseStatus(HttpStatus.OK)
  public Collection<HotelRoomDto> getAllRooms() {
    return hotelRoomsService.findAll();
  }

  @GetMapping(roomsUrl+"/{roomId}")
  @ResponseStatus(HttpStatus.OK)
  public HotelRoomDto getRoomById( @PathVariable Integer roomId) {
    return hotelRoomsService.findById(roomId).get();
  }

  @DeleteMapping(roomsUrl+"/{roomId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteById(@PathVariable Integer roomId) {
    hotelRoomsService.deleteById(roomId);
  }

  @DeleteMapping(roomsUrl)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteAllRooms(){
    hotelRoomsService.deleteAll();
  }

  //Room price

  @PostMapping(pricesUrl)
  @ResponseStatus(HttpStatus.CREATED)
  public RoomPriceDto createRoomPrice(@RequestBody @Validated(value = Groups.Create.class) RoomPriceDto roomPrice){
    return roomPriceService.create(roomPrice);
  }

  @PutMapping(pricesUrl)
  @ResponseStatus(HttpStatus.OK)
  public RoomPriceDto updateRoomPrice(@RequestBody @Validated(value = Groups.Update.class) RoomPriceDto roomPrice){
    return roomPriceService.update(roomPrice, roomPrice.getId());
  }

  @GetMapping(pricesUrl+"/{id}")
  @ResponseStatus(HttpStatus.OK)
  public RoomPriceDto getPrice(@PathVariable Integer id){
    return roomPriceService.findById(id).get();
  }

  @DeleteMapping(pricesUrl+"/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteRoomPriceById(@PathVariable Integer id){
    roomPriceService.deleteById(id);
  }

  @DeleteMapping(pricesUrl)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteAllPrices(){
    roomPriceService.deleteAll();
  }

}
