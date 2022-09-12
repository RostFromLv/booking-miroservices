package com.example.paymentservice.service;


import com.example.commondto.common.CardBalance;
import java.util.Optional;

public interface CardBalanceService {

  Optional<CardBalance> findByNumber(String cardNumber);

  void add(CardBalance cardBalance);

  void withdraw(String cardNumber, Double amount) throws CardOperationException;

  void replenish(String cardNumber, Double amount);

  void remove(String cardNumber);
}
