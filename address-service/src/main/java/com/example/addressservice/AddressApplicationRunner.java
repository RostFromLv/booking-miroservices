package com.example.addressservice;

import brave.internal.Nullable;
import lombok.NonNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

/**
 * The main class the point of starting service.
 * <p></p>
 *
 * @author Rostyslav Balushchak
 */
@SpringBootApplication
public class AddressApplicationRunner {
  public static void main( String[] args) {
    SpringApplication.run(AddressApplicationRunner.class, args);
  }
}
