package com.example.hotelsservice.com.booking;

import com.example.commondto.common.HotelDto;
import com.example.commondto.common.HotelRoomDto;
import com.example.commondto.common.RoomPriceDto;
import com.example.hotelsservice.service.HotelRoomsServiceImpl;
import com.example.hotelsservice.service.HotelServiceImpl;
import com.example.hotelsservice.service.RoomPriceServiceImpl;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HotelControllerV1_IT {

  private  Integer idForGeneratingDto = 1;
  private final static String ID_FIELD_DECLARATION = "id";
  //Hotel data
  private final static String hotelName = "Hotel_NAME";
  private final static Integer addressId = 5;

  //Room data
  private final static Integer roomId = 1;
  private final static Float area = 55f;
  private final static String type = "LUXURY";
  private final Long startDate = 10l;
  private final Long endDate = 50l;
  private final Double roomPrice = 100.0;
  private final Double defaultPrice = 150.0;

  private final HotelServiceImpl hotelService;
  private final HotelRoomsServiceImpl roomsService;
  private final RoomPriceServiceImpl roomPriceService;

  private HotelDto existHotel = new HotelDto();
  private int hotelId;

  @LocalServerPort
  private int port;

  @Autowired
  public HotelControllerV1_IT(
      HotelServiceImpl hotelService,
      HotelRoomsServiceImpl roomsService,
      RoomPriceServiceImpl roomPriceService) {
    this.hotelService = hotelService;
    this.roomsService = roomsService;
    this.roomPriceService = roomPriceService;
    this.existHotel = existHotel;
  }

  @BeforeEach
  void beforeEach() {
    existHotel = hotelService.create(generateHotel());
    hotelId = existHotel.getId();
  }

  @AfterEach
  void afterEach() {
    roomPriceService.deleteAll();
    roomsService.deleteAll();
    hotelService.deleteAll();
    RestAssured.reset();
  }

  //Create hotel
  @Test
  void createHotelByCorrectDto_ShouldReturn_createdHotelDto_And_Status201() {
    HotelDto newHotel = generateHotel().withId(idForGeneratingDto).withAddressId(150);
    HotelDto actual = requestJson()
        .body(newHotel)
        .when()
        .post()
        .then()
        .statusCode(201)
        .extract()
        .as(HotelDto.class);

    Assertions.assertThat(actual)
        .usingRecursiveComparison()
        .ignoringFields(ID_FIELD_DECLARATION)
        .isEqualTo(newHotel);
  }

  @Test
  void createHotelByNotNullDtoId_ShouldThrow_IllegalArgumentException() {
    HotelDto wrongHotelDto = generateHotel().withId(1).withAddressId(150);
    requestJson()
        .body(wrongHotelDto)
        .when()
        .post()
        .then()
        .statusCode(400);
  }

  @Test
  void createHotelWithSameAddressId_ShouldThrow_ConstraintViolationException() {
    HotelDto dtoWithSameAddress = generateHotel().withId(idForGeneratingDto).withName("NewName");
    requestJson()
        .body(dtoWithSameAddress)
        .when()
        .post()
        .then()
        .statusCode(409);
  }

  //Update hotel
  @Test
  void updateHotelByCorrectDto_ShouldReturn_UpdatedDto_And_Status200() {
    HotelDto dtoForUpdate = generateHotel().withId(hotelId).withAddressId(15);
    HotelDto updatedDto = requestJson()
        .body(dtoForUpdate)
        .when()
        .put()
        .then()
        .statusCode(200)
        .extract()
        .as(HotelDto.class);
    org.junit.jupiter.api.Assertions.assertEquals(updatedDto, dtoForUpdate);
  }

  @Test
  void updateHotelByWrongHotelId_ShouldThrow_EntityNotFoundException() {
    HotelDto dtoForUpdate = generateHotel().withId(150);
    requestJson()
        .body(dtoForUpdate)
        .when()
        .put()
        .then()
        .statusCode(404);
  }


  //Get all hotels
  @Test
  void getAllHotels_ShouldReturn_EmptyList() {
    hotelService.deleteAll();
    request()
        .when()
        .get()
        .then()
        .statusCode(200)
        .and()
        .assertThat()
        .body("size()", Matchers.is(0));
  }

  @Test
  void getAllHotels_ShouldReturn_FullList() {
    hotelService.create(generateHotel().withAddressId(100));
    request()
        .when()
        .get()
        .then()
        .statusCode(200)
        .and()
        .body("size()", Matchers.is(2));
  }

  //Get by id hotel
  @Test
  void getHotelById_ShouldReturn_ExistHotel_And_Status_200() {
    HotelDto actual = request()
        .when()
        .get("/" + hotelId)
        .then()
        .statusCode(200)
        .extract()
        .as(HotelDto.class);
    Assertions.assertThat(actual).isEqualTo(existHotel);
  }

  @Test
  void getHotelByWrongId_ShouldThrow_EntityNotFoundException() {
    request()
        .when()
        .get("/1")
        .then()
        .statusCode(404);
  }

  //Delete hotel
  @Test
  void deleteHotelByCorrectId_ShouldReturn_Status204() {
    request()
        .when()
        .delete("/" + hotelId)
        .then()
        .statusCode(204);
  }

  @Test
  void deleteHotelByWrongId_ShouldThrow_EntityNotFoundException() {
    request()
        .when()
        .delete("/" + (hotelId + 100))
        .then()
        .statusCode(404);
  }

  //Create room----------------------------

  @Test
  void createRoomByCorrectDto_ShouldReturn_CreatedDto_And_Status201() {
    HotelRoomDto roomForCreate = generateRoom().withId(idForGeneratingDto).withHotelId(hotelId);

    HotelRoomDto actual = requestJson()
        .body(roomForCreate)
        .when()
        .post("/rooms")
        .then()
        .statusCode(201)
        .and()
        .extract()
        .as(HotelRoomDto.class);

    Assertions.assertThat(actual)
        .usingRecursiveComparison()
        .ignoringFields(ID_FIELD_DECLARATION)
        .isEqualTo(roomForCreate);

  }

  @Test
  void createRoomByNotNullDtoId_ShouldThrow_IllegalArgumentException() {
    HotelRoomDto roomForCreate = generateRoom();

    requestJson()
        .body(roomForCreate)
        .when()
        .post("/rooms")
        .then()
        .statusCode(400);
  }

  //Update room
  @Test
  void updateRoomByCorrectId_ShouldReturn_updatedRoomDto_And_Status200() {
    HotelRoomDto createdRoom = roomsService.create(generateRoom().withId(idForGeneratingDto));
    HotelRoomDto dtoForUpdate = generateRoom().withId(createdRoom.getId()).withHotelId(idForGeneratingDto);

    HotelRoomDto actual = requestJson()
        .body(dtoForUpdate)
        .when()
        .put("/rooms")
        .then()
        .statusCode(200)
        .extract()
        .as(HotelRoomDto.class);

    Assertions.assertThat(actual).isEqualTo(dtoForUpdate.withHotelId(existHotel.getId()));
  }

  @Test
  void updateRoomByWrongRoomId_ShouldThrow_EntityNotFoundException() {
    HotelRoomDto dtoForUpdate = generateRoom().withId(500).withHotelId(idForGeneratingDto);

    requestJson()
        .body(dtoForUpdate)
        .when()
        .put("/rooms")
        .then()
        .statusCode(404);
  }


  //Get all room
  @Test
  void getAllRoom_ShouldReturn_EmptyList() {
    request()
        .when()
        .get("/rooms")
        .then()
        .statusCode(200)
        .and()
        .body("size()", Matchers.is(0));

  }

  @Test
  void getAllRoom_ShouldReturn_FullList() {
    roomsService.create(generateRoom().withId(idForGeneratingDto));
    roomsService.create(generateRoom().withId(1001));

    request().when()
        .get("/rooms")
        .then()
        .statusCode(200)
        .and()
        .body("size()", Matchers.is(2));

  }

  //Get by id room
  @Test
  void getRoomByCorrectId_ShouldReturn_ExistRoomDto_And_Status200() {
    HotelRoomDto existRoom = roomsService.create(generateRoom().withId(idForGeneratingDto));

    HotelRoomDto actual = request()
        .when()
        .get("/rooms/" + existRoom.getId())
        .then()
        .statusCode(200)
        .extract()
        .as(HotelRoomDto.class);

    org.junit.jupiter.api.Assertions.assertEquals(existRoom, actual);
  }

  @Test
  void getRoomByWrongId_ShouldThrow_EntityNotFoundException() {
    request().when()
        .get("/rooms/" + 1)
        .then()
        .statusCode(404);
  }


  //Delete by id room
  @Test
  void deleteByCorrectDto_ShouldReturn_Status204() {
    HotelRoomDto existRoom = roomsService.create(generateRoom().withId(idForGeneratingDto));
    request()
        .when()
        .delete("/rooms/" + existRoom.getId())
        .then()
        .statusCode(204);
  }

  @Test
  void deleteByWrongId_ShouldThrow_EntityNotFoundException() {
    request()
        .when()
        .delete("/rooms/" + 1)
        .then()
        .statusCode(404);
  }


  //RoomPrice Tests
  //Post
  @Test
  void createRoomPriceByCorrectData_ShouldReturn_Status201_and_CreatedRoomPriceDto() {
    //
    HotelDto hotelDto = hotelService.create(generateHotel().withId(idForGeneratingDto).withAddressId(1));
    HotelRoomDto roomDto =
        roomsService.create(generateRoom().withId(idForGeneratingDto).withHotelId(hotelDto.getId()));

    RoomPriceDto actual = requestJson()
        .body(createRoomPriceDto())
        .when()
        .post("/prices")
        .then()
        .statusCode(201)
        .and()
        .extract()
        .as(RoomPriceDto.class);

    org.junit.jupiter.api.Assertions.assertNotNull(actual);
    org.junit.jupiter.api.Assertions.assertEquals(actual.getPrice(), roomPrice);
  }

  //Put

  @Test
  void updateByCorrectData_ShouldReturn_Status200_and_updatedRoomPriceDto() {
    HotelRoomDto roomDto = roomsService.create(generateRoom().withId(idForGeneratingDto).withHotelId(hotelId));
    RoomPriceDto createdPrice = roomPriceService.create(createRoomPriceDto());
//
    RoomPriceDto actual = requestJson()
        .body(createdPrice.withPrice(roomPrice + 100).withEndDate(5l).withStartDate(1l))
        .when()
        .put("/prices")
        .then()
        .statusCode(200)
        .extract()
        .as(RoomPriceDto.class);

    org.junit.jupiter.api.Assertions.assertNotNull(actual);
    Assertions.assertThat(actual)
        .usingRecursiveComparison()
        .ignoringFields("price", "endDate")
        .isEqualTo(createdPrice);

  }

  //Get
  @Test
  void getByCorrectId_ShouldReturn_existRoomPriceDto_and_Status200() {
    HotelDto hotelDto = hotelService.create(generateHotel().withAddressId(1));
    HotelRoomDto roomDto =
        roomsService.create(generateRoom().withId(idForGeneratingDto).withHotelId(hotelDto.getId()));
    RoomPriceDto createdRoomPriceDto = roomPriceService.create(createRoomPriceDto());
//
    RoomPriceDto actual = request().when()
        .get("/prices/" + createdRoomPriceDto.getId())
        .then()
        .statusCode(200)
        .and()
        .extract()
        .as(RoomPriceDto.class);

    org.junit.jupiter.api.Assertions.assertEquals(actual, createdRoomPriceDto);

  }

  @Test
  void getByWrongId_ShouldThrow_status404() {
    request().when()
        .get("/prices/1")
        .then()
        .statusCode(404);
  }

  //Delete
  @Test
  void deleteByCorrectId_ShouldReturn_status204() {
    HotelDto hotelDto = hotelService.create(generateHotel().withAddressId(1));
    HotelRoomDto roomDto =
        roomsService.create(generateRoom().withId(idForGeneratingDto).withHotelId(hotelDto.getId()));
    RoomPriceDto createdRoomPriceDto = roomPriceService.create(createRoomPriceDto());
//
    request().when()
        .delete("/prices/" + createdRoomPriceDto.getId())
        .then()
        .statusCode(204);
  }

  @Test
  void deleteByWrongId_ShouldThrow_Status404() {
    request().when()
        .delete("/prices/1")
        .then()
        .statusCode(404);
  }

  private HotelDto generateHotel() {
    HotelDto hotel = new HotelDto();

    hotel.setId(idForGeneratingDto++);
    hotel.setAddressId(addressId);
    hotel.setName(hotelName);

    return hotel;
  }

  private HotelRoomDto generateRoom() {
    HotelRoomDto roomDto = new HotelRoomDto();

    roomDto.setHotelId(existHotel.getId());
    roomDto.setId(roomId);
    roomDto.setArea(area);
    roomDto.setType(type);

    return roomDto;
  }

  private RoomPriceDto createRoomPriceDto() {
    RoomPriceDto roomPriceDto = new RoomPriceDto();
    roomPriceDto.setId(100);
    roomPriceDto.setPrice(roomPrice);
    roomPriceDto.setDefaultPrice(defaultPrice);
    roomPriceDto.setStartDate(startDate);
    roomPriceDto.setEndDate(endDate);
    return roomPriceDto;
  }

  private RequestSpecification request() {
    return RestAssured.given().port(port).basePath("/api/v1/hotels");
  }

  private RequestSpecification requestJson() {
    return request().contentType(ContentType.JSON);
  }
}
