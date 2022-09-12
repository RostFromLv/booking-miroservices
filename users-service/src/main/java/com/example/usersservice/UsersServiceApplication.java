package com.example.usersservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class UsersServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(UsersServiceApplication.class, args);
  }

}
