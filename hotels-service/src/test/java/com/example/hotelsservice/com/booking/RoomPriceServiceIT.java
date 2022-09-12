package com.example.hotelsservice.com.booking;

import com.example.commondto.common.RoomPriceDto;
import com.example.hotelsservice.service.HotelRoomService;
import com.example.hotelsservice.service.HotelService;
import com.example.hotelsservice.service.RoomPriceServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("test")
public class RoomPriceServiceIT {

  private final Double price = 100D;
  private final Double defaultPrice = 150D;
  private final Long startDate = 10L;
  private final Long endDate = 100L;

  private final RoomPriceServiceImpl roomPriceService;
  private final HotelRoomService roomService;
  private final HotelService hotelService;

  @Autowired
  public RoomPriceServiceIT(RoomPriceServiceImpl roomPriceService, HotelRoomService roomService, HotelService hotelService) {
    this.roomPriceService = roomPriceService;
    this.roomService = roomService;
    this.hotelService = hotelService;
  }

  @BeforeEach
  void beforeEach() {
    hotelService.deleteAll();
    roomService.deleteAll();
    roomPriceService.deleteAll();
  }

  //Post
  @Test
  void createByCorrectData_ShouldReturn_CreatedRoomPrice() {
    RoomPriceDto actual = roomPriceService.create(createRoomPrice());
    Assertions.assertThat(actual)
          .usingRecursiveComparison()
          .ignoringFields("id")
          .isEqualTo(createRoomPrice());
  }

  @Test
  void createByStartDateBiggerThanEndDate_ShouldThrow_IllegalArgumentException() {
    org.junit.jupiter.api.Assertions.assertThrows(
          IllegalArgumentException.class,
          () -> roomPriceService.create(createCustomRoomPrice( 50.0, 10L, 5L)));
  }

  //Put
  @Test
  void updateByCorrectData_ShouldReturn_UpdatedRoomPrice() {

    RoomPriceDto existRoomPrice = roomPriceService.create(createRoomPrice());
    RoomPriceDto updatedRoomPrice = roomPriceService.update(existRoomPrice.withDefaultPrice(200.0).withPrice(185.0), existRoomPrice.getId());
    Assertions.assertThat(existRoomPrice)
          .usingRecursiveComparison()
          .ignoringFields("defaultPrice", "price")
          .isEqualTo(updatedRoomPrice);

  }


  //Get by id
  @Test
  void getByCorrectId_ShouldReturn_ExistRoomPrice() {

    RoomPriceDto existRoomPrice = roomPriceService.create(createRoomPrice());
    org.junit.jupiter.api.Assertions.assertEquals(existRoomPrice, roomPriceService.findById(existRoomPrice.getId()).get());
  }


  //Delete
  @Test
  void deleteByCorrectId_ShouldDelete_ExistEntity() {

    RoomPriceDto existRoomPrice = roomPriceService.create(createRoomPrice());
    roomPriceService.deleteById(existRoomPrice.getId());
    org.junit.jupiter.api.Assertions.assertTrue(roomPriceService.findAll().isEmpty());
  }

  @Test
  void deleteByWrongId_ShouldThrow_EntityExistException() {
    org.junit.jupiter.api.Assertions.assertThrows(EmptyResultDataAccessException.class, () -> roomPriceService.deleteById(1));
  }

  private RoomPriceDto createRoomPrice() {
    RoomPriceDto roomPriceDto = new RoomPriceDto();
    roomPriceDto.setPrice(price);
    roomPriceDto.setDefaultPrice(defaultPrice);
    roomPriceDto.setStartDate(startDate);
    roomPriceDto.setEndDate(endDate);
    return roomPriceDto;
  }

  private RoomPriceDto createCustomRoomPrice(Double roomPrice, Long startDate, Long endDate) {
    return createRoomPrice().withPrice(roomPrice).withStartDate(startDate).withEndDate(endDate);
  }

}
