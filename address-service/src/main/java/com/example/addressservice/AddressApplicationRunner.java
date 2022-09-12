package com.example.addressservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main class the point of starting service.
 * <p></p>
 *
 * @author Rostyslav Balushchak
 */
@SpringBootApplication
public class AddressApplicationRunner {
  public static void main(String[] args) {
    SpringApplication.run(AddressApplicationRunner.class, args);
  }
}
