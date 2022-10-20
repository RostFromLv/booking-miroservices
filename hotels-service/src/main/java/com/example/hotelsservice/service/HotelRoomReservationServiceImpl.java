package com.example.hotelsservice.service;

import com.example.bookingcommonabstractservice.service.AbstractDataService;
import com.example.commondto.common.ReservationDto;
import com.example.hotelsservice.domain.Reservation;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class HotelRoomReservationServiceImpl extends AbstractDataService<Integer, Reservation, ReservationDto>
    implements HotelRoomReservationService {

  private final static String nullReservationMessage = "ReservationDto cannot be bull";
  private final static String wrongDatesReservationMessage = "Reservation start date should be less than finish date";
  private final static String dateFromInReservedIntervalMessage = "Cannot book room. Your start date is not available to book.";
  private final static String dateToInReservedIntervalMessage = "Cannot book room. Your end date is not available to book.";
  private final static String reservationDateIsAroundBookedDatesMessage = "Cannot book room. Cannot book all selected dates.";


  @Override
  public ReservationDto create(@NotNull ReservationDto target) {
    Assert.isTrue(target.getDateFrom() < target.getDateTo(), wrongDatesReservationMessage);
    findAll().forEach(reservationDto -> existSameReservation(reservationDto, target));
    return super.create(target);
  }

  //Check methods
  private void existSameReservation(ReservationDto reservationDB, ReservationDto reservationFromUser) {

    if (equalsRoomDataFromReservations(reservationDB, reservationFromUser)) {
      checkDateBounds(reservationDB,reservationFromUser);
    }
  }

  private boolean equalsRoomDataFromReservations(ReservationDto reservationDB, ReservationDto reservationFromUser) {

    return (reservationDB.getHotelId().equals(reservationFromUser.getHotelId())
          &&
          reservationDB.getRoomId().equals(reservationFromUser.getRoomId()));
  }

  private void checkDateBounds(ReservationDto source, ReservationDto target) {

    Long srcTo = source.getDateTo();
    Long srcFrom = source.getDateFrom();
    Long targetTo = target.getDateTo();
    Long targetFrom = target.getDateFrom();

    if (srcFrom<=targetFrom && srcTo>= targetFrom){
      throw new IllegalArgumentException(dateFromInReservedIntervalMessage);
    }
    if (srcFrom<=targetTo && srcTo>= targetTo){
      throw new IllegalArgumentException(dateToInReservedIntervalMessage);
    }
    if (srcFrom>=targetFrom && srcTo<= targetTo){
      throw new IllegalArgumentException(reservationDateIsAroundBookedDatesMessage);
    }
  }
}