package com.example.paymentservice.configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.TestConfiguration;
import redis.embedded.RedisServer;

@TestConfiguration
public class TestRedisConfiguration {

  private final RedisServer redisServer;

  @Autowired
  public TestRedisConfiguration(RedisProperties redisProperties) {
    this.redisServer = new RedisServer(redisProperties.getPort());
  }

  @PostConstruct
  public void postConstruct(){
    redisServer.start();
  }
  @PreDestroy
  public void preDestroy(){
    redisServer.stop();
  }


}
