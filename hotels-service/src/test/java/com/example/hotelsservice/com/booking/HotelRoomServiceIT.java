package com.example.hotelsservice.com.booking;

import com.example.commondto.common.HotelDto;
import com.example.commondto.common.HotelRoomDto;
import com.example.hotelsservice.service.HotelRoomsServiceImpl;
import com.example.hotelsservice.service.HotelServiceImpl;
import javax.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class HotelRoomServiceIT {

  private  Integer idForGeneratingDto = 1;
  //Hotel data
  private final static Integer HOTEL_ID_1 = 1;
  private final static String HOTEL_NAME_Resort = "Resort";
  private final static Integer ADDRESS_HOTEL_ID_25 = 25;

  //Room data
  private final static Integer ID_1 = 1;
  private final static Float AREA_60 = 60F;
  private final static String TYPE_Luxury = "Luxury";

  private final HotelServiceImpl hotelService;
  private final HotelRoomsServiceImpl roomService;

  @Autowired
  public HotelRoomServiceIT(HotelServiceImpl hotelService,
                            HotelRoomsServiceImpl hotelRoomsService) {
    this.hotelService = hotelService;
    this.roomService = hotelRoomsService;
  }

  @BeforeEach
  void beforeEach() {
    hotelService.deleteAll();
    roomService.deleteAll();
  }

  //Create
  @Test
  void createByCorrectRoomDto_ShouldReturn_CreatedRoomDto() {
    HotelDto createdHotel = hotelService.create(generateHotel());
    System.out.println(createdHotel.toString());
    int hotelId = createdHotel.getId();
    HotelRoomDto createRoomDto = generateRoom().withId(idForGeneratingDto).withHotelId(hotelId);

    HotelRoomDto actual = roomService.create(createRoomDto);
    Assert.notNull(actual);

    Assertions.assertThat(actual)
          .usingRecursiveComparison()
          .ignoringFields("id")
          .isEqualTo(createRoomDto);
  }

  @Test
  void createByWrongHotelId_ShouldThrow_EntityNotFoundException() {
    org.junit.jupiter.api.Assertions.assertThrows(
          EntityNotFoundException.class,
          () -> roomService.create(generateRoom().withId(idForGeneratingDto)));
  }

  //Update
  @Test
  void updateByWrongHotelId_ShouldThrow_EntityNotFoundException() {
    org.junit.jupiter.api.Assertions.assertThrows(EntityNotFoundException.class, () -> roomService.update(generateRoom(), 1));
  }

  @Test
  void updateByNotExistId_ShouldThrow_EntityNotFoundException() {
    HotelDto existHotel = hotelService.create(generateHotel());
    org.junit.jupiter.api.Assertions.assertThrows(
          EntityNotFoundException.class,
          () -> roomService.update(generateRoom(), existHotel.getId()));
  }

  //GetById
  @Test
  void getByCorrectId_ShouldReturn_ExistRoomDto() {
    HotelDto existHotel = hotelService.create(generateHotel());
    HotelRoomDto roomDto = roomService.create(generateRoom().withId(idForGeneratingDto).withHotelId(existHotel.getId()));
    Assertions.assertThat(roomDto).isEqualTo(roomService.findById(roomDto.getId()).get());
  }

  @Test
  void getByWrongRoomId_ShouldThrow_EntityNotFoundException() {
    org.junit.jupiter.api.Assertions.assertThrows(EntityNotFoundException.class,()->roomService.findById(1).get());
  }


  //GetAll
  @Test
  void getAll_ShouldReturn_FullList() {
    HotelDto existHotel = hotelService.create(generateHotel());
    int hotelId = existHotel.getId();
    roomService.create(generateRoom().withId(idForGeneratingDto).withHotelId(hotelId));
    roomService.create(generateRoom().withId(idForGeneratingDto).withHotelId(hotelId));
    org.junit.jupiter.api.Assertions.assertEquals(2, roomService.findAll().size());
  }

  @Test
  void getAll_ShouldReturn_EmptyList() {
    HotelDto existHotel = hotelService.create(generateHotel());
    org.junit.jupiter.api.Assertions.assertEquals(0, roomService.getAllRoomsByHotelId(existHotel.getId()).size());
  }

  @Test
  void getAllByWrongHotelId_ShouldThrow_EntityNotFoundException() {
    HotelDto existHotel = hotelService.create(generateHotel());
    roomService.create(generateRoom().withId(idForGeneratingDto).withHotelId(existHotel.getId()));
    org.junit.jupiter.api.Assertions.assertThrows(EntityNotFoundException.class, () -> roomService.getAllRoomsByHotelId( idForGeneratingDto));
  }

  //Delete
  @Test
  void deleteByCorrectId_Should_VerifyCall() {
    HotelDto existHotel = hotelService.create(generateHotel());

    HotelRoomDto existRoom = roomService.create(generateRoom().withId(idForGeneratingDto).withHotelId(existHotel.getId()));
    roomService.deleteById(existRoom.getId());
    org.junit.jupiter.api.Assertions.assertFalse(roomService.findById(existRoom.getId()).isPresent());
  }

  @Test
  void deleteByWrongId_ShouldThrow_EmptyResultDataAccessException() {
    HotelDto existHotel = hotelService.create(generateHotel());
    org.junit.jupiter.api.Assertions.assertThrows(EmptyResultDataAccessException.class, () -> roomService.deleteById(existHotel.getId()));
  }


  HotelRoomDto generateRoom() {
    HotelRoomDto hotelRoomDto = new HotelRoomDto();

    hotelRoomDto.setHotelId(HOTEL_ID_1);
    hotelRoomDto.setArea(AREA_60);
    hotelRoomDto.setType(TYPE_Luxury);
    hotelRoomDto.setId(ID_1);

    return hotelRoomDto;
  }

  HotelDto generateHotel() {
    HotelDto hotelDto = new HotelDto();

    hotelDto.setName(HOTEL_NAME_Resort);
    hotelDto.setAddressId(ADDRESS_HOTEL_ID_25);

    return hotelDto;
  }



}
