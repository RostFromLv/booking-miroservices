package com.example.springbootexceptionhandlerstarter;

import com.example.com.booking.RestExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(RestExceptionHandler.class)
public class ConfigClass {

  @Bean
  @ConditionalOnMissingBean
  public RestExceptionHandler getHandler(){
    return new RestExceptionHandler();
  }
}