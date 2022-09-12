package com.example.ordersservice.utils;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class WireHotelMockConfig {
  @Bean(initMethod = "start",destroyMethod = "stop")
  public WireMockServer mockServer(){
    return new WireMockServer(8083);
  }
}
