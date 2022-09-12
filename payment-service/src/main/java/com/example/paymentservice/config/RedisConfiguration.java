package com.example.paymentservice.config;

import com.example.commondto.common.CardBalance;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfiguration {

  @Bean
  public RedisTemplate<String, CardBalance> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, CardBalance> template = new RedisTemplate<>();
    template.setConnectionFactory(redisConnectionFactory);
    return template;
  }
}
