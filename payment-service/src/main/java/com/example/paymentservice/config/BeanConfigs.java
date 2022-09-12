package com.example.paymentservice.config;

import com.example.paymentservice.service.FactoryPaymentExecutor;
import com.example.paymentservice.service.PaymentExecutor;
import com.example.paymentservice.service.PaymentServiceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class BeanConfigs {

  @Bean
  @Primary
  PaymentExecutor paymentExecutor(PaymentServiceFactory paymentService){
    return new FactoryPaymentExecutor(paymentService);
  }
}
