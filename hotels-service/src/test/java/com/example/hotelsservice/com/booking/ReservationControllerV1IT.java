package com.example.hotelsservice.com.booking;

import com.example.commondto.common.ReservationDto;
import com.example.hotelsservice.service.HotelRoomReservationServiceImpl;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReservationControllerV1IT {

  private final Integer hotelId_1 = 1;
  private final Integer roomId_1 = 1;
  private final Long dateFrom_10 = 10L;
  private final Long dateTo_100 = 100L;


  private final HotelRoomReservationServiceImpl reservationService;

  @LocalServerPort
  private int port;

  @Autowired
  public ReservationControllerV1IT(HotelRoomReservationServiceImpl reservationService) {
    this.reservationService = reservationService;
  }

  @BeforeEach
  void beforeEach(){
   reservationService.deleteAll();
  }
  //Create
  @Test
  void reservationWithCorrectData_ShouldReturn_ReservationDto_and_Status201(){
    ReservationDto createdReservation = requestJson()
          .body(generateReservationDto())
          .when()
          .post()
          .then()
          .statusCode(201)
          .extract()
          .as(ReservationDto.class);

    Assertions.assertThat(createdReservation)
          .usingRecursiveComparison()
          .ignoringFields("id")
          .isEqualTo(generateReservationDto());
  }
  @Test
  void createReservationWithDateFromInReservedDate_ShouldReturn_Status409(){
    reservationService.create(generateReservationDto());
    ReservationDto incorrectReservationDto = generateCustomReservationDto(hotelId_1,roomId_1,dateFrom_10+20,dateTo_100+10);
    requestJson()
          .body(incorrectReservationDto)
          .when()
          .post()
          .then()
          .statusCode(400);
  }
  @Test
  void createReservationWithDateToInReservedDate_ShouldReturn_Status409(){
    reservationService.create(generateReservationDto());
    ReservationDto incorrectReservationDto = generateCustomReservationDto(hotelId_1,roomId_1,dateFrom_10-50,dateTo_100-50);
    requestJson()
          .body(incorrectReservationDto)
          .when()
          .post()
          .then()
          .statusCode(400);
  }
  @Test
  void createReservationAroundReservedInterval_ShouldReturn_Status409(){
    reservationService.create(generateReservationDto());
    ReservationDto incorrectReservationDto = generateCustomReservationDto(hotelId_1,roomId_1,dateFrom_10-10,dateTo_100+10);
    requestJson()
          .body(incorrectReservationDto)
          .when()
          .post()
          .then()
          .statusCode(400);
  }
  //Remove
  @Test
  void removeReservationByNotExistReservationId_ShouldThrow_Status404(){
    request()
          .when()
          .delete("/0")
          .then()
          .statusCode(404);
  }
  @Test
  void removeReservationByCorrectData_ShouldReturn_Status204(){
    ReservationDto existReservation = reservationService.create(generateReservationDto());
    request()
          .when()
          .delete("/"+existReservation.getId())
          .then()
          .statusCode(204);
  }


  private  ReservationDto generateReservationDto(){
    ReservationDto reservationDto = new ReservationDto();
    Integer reservationId_1 = 1;
    reservationDto.setId(reservationId_1);
    reservationDto.setHotelId(hotelId_1);
    reservationDto.setRoomId(roomId_1);
    reservationDto.setDateFrom(dateFrom_10);
    reservationDto.setDateTo(dateTo_100);
    return reservationDto;
  }
  private  ReservationDto generateCustomReservationDto(Integer hotelId,Integer roomId,Long dateFrom,Long dateTo){
    return generateReservationDto().withHotelId(hotelId).withRoomId(roomId).withDateFrom(dateFrom).withDateTo(dateTo);
  }

  private RequestSpecification request() {
    return RestAssured.given().port(port).basePath("/api/v1/hotels/reservations");
  }

  private RequestSpecification requestJson() {
    return request().contentType(ContentType.JSON);
  }
}
