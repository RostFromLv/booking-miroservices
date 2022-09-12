package com.example.paymentservice.service;

import com.example.commondto.common.CardBalance;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class RedisCardBalanceServiceImpl implements CardBalanceService {

  private static final String key = "payment:card:balances";
  private final static String nullCardNumberMessage = "Card number can`t be null";
  private final static String nullCardBalanceMessage = "Card balance can`t be null";
  private final static String wrongCardNumber = "Card number does`nt exist: ";
  private final static String negativeAmount = "Amount  cannot be less than 0";


  private final RedisTemplate<String, CardBalance> template;

  private HashOperations<String, String, CardBalance> ops;

  @Autowired
  public RedisCardBalanceServiceImpl(RedisTemplate<String, CardBalance> template) {
    this.template = template;
    this.ops = this.template.opsForHash();
  }

  @Override
  public Optional<CardBalance> findByNumber(String cardNumber) {
    Assert.notNull(cardNumber, nullCardNumberMessage);

    Optional<CardBalance> result = Optional.ofNullable(this.ops.get(key, cardNumber));

    if(result.isEmpty()){
      throw new  EntityNotFoundException("Entity not found with card number: "+cardNumber);
    }
    return result;
  }

  @Override
  public void add(CardBalance cardBalance) {

    Assert.notNull(cardBalance, nullCardBalanceMessage);

    this.ops.put(key, cardBalance.getCardNumber(), cardBalance);
  }

  @Override
  public void withdraw(String cardNumber, Double amount) throws CardOperationException {

    CardBalance cardBalance = this.ops.get(key, cardNumber);


    Assert.notNull(cardBalance, wrongCardNumber+ cardNumber);
    Assert.isTrue(amount>0,negativeAmount);

    Double balance = cardBalance.getBalance();
    if (balance < amount) {
      throw new CardOperationException("Not enough money.Check your card cash.");
    }
    cardBalance.setBalance(balance - amount);
    this.ops.put(key, cardNumber, cardBalance);
  }

  @Override
  public void replenish(String cardNumber, Double amount){

    CardBalance cardBalance = this.ops.get(key, cardNumber);
    Assert.notNull(cardBalance, wrongCardNumber);
    Assert.isTrue(amount>0,negativeAmount);
    Double balance = cardBalance.getBalance();

    cardBalance.setBalance(balance + amount);
    this.ops.put(key, cardNumber, cardBalance);
  }

  @Override
  public void remove(String cardNumber) {
    Assert.notNull(cardNumber, nullCardNumberMessage);

    if (findByNumber(cardNumber).isEmpty()){
      throw new EntityNotFoundException(wrongCardNumber+cardNumber);
    }
    this.ops.delete(key,cardNumber);
  }

}

