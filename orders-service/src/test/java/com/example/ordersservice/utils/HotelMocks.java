package com.example.ordersservice.utils;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static java.nio.charset.Charset.defaultCharset;
import static org.springframework.util.StreamUtils.copyToString;

import com.github.tomakehurst.wiremock.WireMockServer;
import java.io.IOException;
import javax.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class HotelMocks {
  public static void setUpMockHotelResponse(WireMockServer mockServer) throws IOException {
    ClassLoader hotelClassLoader = HotelMocks.class.getClassLoader();
    if (hotelClassLoader == null){
      throw new NullPointerException();
    }

    mockServer.stubFor(post(urlEqualTo("/api/v1/hotels/reservations"))
          .willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(
                      copyToString(
                            hotelClassLoader.getResourceAsStream("post-reservation-response.json"),
                            defaultCharset()))));

  }
  public static void setUpErrorMockHotelResponse(WireMockServer mockServer) throws IOException {
    mockServer.stubFor(post(urlEqualTo("/api/v1/hotels/reservations")).willReturn(
          aResponse()
                .withStatus(HttpStatus.BAD_REQUEST.value())));
  }
}
