package com.example.hotelsservice.rest;

import com.example.commondto.common.ReservationDto;
import com.example.hotelsservice.service.HotelRoomReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/hotels/reservations")
public class ReservationControllerV1 {

  private final HotelRoomReservationService reservationService;

  @Autowired
  public ReservationControllerV1(HotelRoomReservationService reservationService) {
    this.reservationService = reservationService;
  }

  @PostMapping()
  @ResponseStatus(HttpStatus.CREATED)
  public ReservationDto create(@RequestBody ReservationDto reservationDto) {
    return reservationService.create(reservationDto);
  }

  //Delete
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Integer id) {
    reservationService.deleteById(id);
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteAll(){
    reservationService.deleteAll();
  }
}
