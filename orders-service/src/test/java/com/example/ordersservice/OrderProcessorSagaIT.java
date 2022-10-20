package com.example.ordersservice;

import com.example.commondto.common.ReservationDto;
import com.example.ordersservice.service.HotelFeignClient;
import com.example.ordersservice.utils.HotelMocks;
import com.example.ordersservice.utils.WireHotelMockConfig;
import com.github.tomakehurst.wiremock.WireMockServer;
import feign.FeignException;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ActiveProfiles("test")
@EnableConfigurationProperties
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = WireHotelMockConfig.class)
public class OrderProcessorSagaIT {

  @Autowired
  private WireMockServer mockServer;

  private final HotelFeignClient hotelClient;

  private ReservationDto reservationDto = new ReservationDto();


  @Autowired
  public OrderProcessorSagaIT(WireMockServer mockServer,
                              HotelFeignClient hotelClient) {
    this.mockServer = mockServer;
    this.hotelClient = hotelClient;
  }



  @BeforeEach
  void setUp() throws IOException {
    HotelMocks.setUpMockHotelResponse(mockServer);
     reservationDto = generateReservationDto(1, 1, 1, 1L, 5L);
  }

  @Test
  void createReservationByCorrectData_ShouldReturn_createdReservation() {

    ReservationDto actual = hotelClient.createReservation(reservationDto);
    Assertions.assertEquals(reservationDto,actual);
  }

  @Test
  void createReservationAroundReservedDates_ShouldThrow_IllegalArgumentException() throws IOException {
    HotelMocks.setUpErrorMockHotelResponse(mockServer);
    FeignException exception = Assertions.assertThrows(FeignException.class,
          () -> hotelClient.createReservation(reservationDto.withId(2).withDateFrom(2L).withDateTo(8L)));
    int status = exception.status();
    Assertions.assertEquals(status, HttpStatus.BAD_REQUEST.value());
  }


  ReservationDto generateReservationDto(Integer id, Integer hotelId, Integer roomId, Long fromDate, Long dateTo) {
    return new ReservationDto()
          .withId(id)
          .withHotelId(hotelId)
          .withRoomId(roomId)
          .withDateFrom(fromDate)
          .withDateTo(dateTo);
  }
}
