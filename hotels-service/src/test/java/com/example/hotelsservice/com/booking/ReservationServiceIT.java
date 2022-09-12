package com.example.hotelsservice.com.booking;

import com.example.commondto.common.ReservationDto;
import com.example.hotelsservice.service.HotelRoomReservationServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("test")
public class ReservationServiceIT {

  private final Integer reservationId_1 = 1;
  private final Integer reservationId_2 = 2;
  private final Integer hotelId_1 = 1;
  private final Integer roomId_1 = 1;
  private final Long dateFrom_10 = 10L;
  private final Long dateTo_100 = 100L;


  private final HotelRoomReservationServiceImpl reservationService;

  @Autowired
  public ReservationServiceIT(HotelRoomReservationServiceImpl reservationService) {
    this.reservationService = reservationService;
  }

  @BeforeEach
  void beforeEach(){
    reservationService.deleteAll();
  }

  //Create reservation
  @Test
  void createReservationByCorrectData_ShouldReturn_CreatedReservation(){
      ReservationDto actual  =  reservationService.create(generateReservationDto());
      Assertions.assertEquals(generateReservationDto(),actual);
  }
  @Test
  void createReservationByNullReservationDto_ShouldThrow_IllegalArgumentException(){
    Assertions.assertThrows(IllegalArgumentException.class,()->reservationService.create(null));
  }

  @Test
  void createReservationByWrongStartAndFinishDate_ShouldThrow_IllegalArgumentException(){ //When creating date is bigger than finish
    Assertions.assertThrows(IllegalArgumentException.class,
          ()-> reservationService.create(generateCustomReservationDto(reservationId_1,hotelId_1,roomId_1,dateTo_100,dateFrom_10)));
  }
  @Test
  void createReservationByExistDateFrom_ShouldThrow_EntityExistException(){//Right border
    reservationService.create(generateReservationDto());
    ReservationDto newReservation = generateCustomReservationDto(reservationId_2,hotelId_1,roomId_1,dateTo_100-50,dateTo_100+50);
    Assertions.assertThrows(IllegalArgumentException.class,()->reservationService.create(newReservation));
  }
  @Test
  void createReservationByExistDateTo_ShouldThrow_EntityExistException(){//Left border
      reservationService.create(generateReservationDto());
      ReservationDto newReservation = generateCustomReservationDto(reservationId_2,hotelId_1,roomId_1,dateFrom_10-50,dateTo_100-50);
      Assertions.assertThrows(IllegalArgumentException.class,()->reservationService.create(newReservation));
  }
  @Test
  void createReservationAroundExistInterval_ShouldThrow_EntityExistException(){ //inside
    reservationService.create(generateReservationDto());
    ReservationDto newReservation = generateCustomReservationDto(reservationId_2,hotelId_1,roomId_1,dateFrom_10+10,dateTo_100-50);
    Assertions.assertThrows(IllegalArgumentException.class,()->reservationService.create(newReservation));
  }
  @Test
  void createReservationInsideExistInterval_ShouldThrow_EntityExistException(){ //around
    reservationService.create(generateReservationDto());
    ReservationDto newReservation = generateCustomReservationDto(reservationId_2,hotelId_1,roomId_1,dateFrom_10-10,dateTo_100+50);
    Assertions.assertThrows(IllegalArgumentException.class,()->reservationService.create(newReservation));
  }

  //Remove reservation
  @Test
  void removeReservation_Should_RemoveReservation(){
    ReservationDto reservation = reservationService.create(generateReservationDto());
    reservationService.deleteById(reservation.getId());
    Assertions.assertTrue(reservationService.findAll().isEmpty());
  }

  @Test
  void removeReservationByDontExistReservationId_ShouldThrow_EntityExistException(){
    Assertions.assertThrows(EmptyResultDataAccessException.class,()->reservationService.deleteById(1));
  }

  private  ReservationDto generateReservationDto(){
    ReservationDto reservationDto = new ReservationDto();
    reservationDto.setId(reservationId_1);
    reservationDto.setHotelId(hotelId_1);
    reservationDto.setRoomId(roomId_1);
    reservationDto.setDateFrom(dateFrom_10);
    reservationDto.setDateTo(dateTo_100);
    return reservationDto;
  }
  private  ReservationDto generateCustomReservationDto(Integer id ,Integer hotelId,Integer roomId,Long dateFrom,Long dateTo){
    return generateReservationDto().withId(id).withHotelId(hotelId).withRoomId(roomId).withDateFrom(dateFrom).withDateTo(dateTo);
  }
}
