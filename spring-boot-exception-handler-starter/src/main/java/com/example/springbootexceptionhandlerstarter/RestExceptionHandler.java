package com.example.springbootexceptionhandlerstarter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(RestExceptionHandler.class)
public class RestExceptionHandler {

}