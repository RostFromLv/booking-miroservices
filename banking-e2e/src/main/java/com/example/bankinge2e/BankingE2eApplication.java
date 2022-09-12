package com.example.bankinge2e;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class BankingE2eApplication {

  public static void main(String[] args) {
    SpringApplication.run(BankingE2eApplication.class, args);
  }

}
