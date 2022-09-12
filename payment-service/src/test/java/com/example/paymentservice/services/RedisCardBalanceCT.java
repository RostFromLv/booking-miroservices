package com.example.paymentservice.services;

import com.example.commondto.common.CardBalance;
import com.example.paymentservice.configuration.TestRedisConfiguration;
import com.example.paymentservice.service.CardOperationException;
import com.example.paymentservice.service.RedisCardBalanceServiceImpl;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestRedisConfiguration.class)
public class RedisCardBalanceCT {

  private final RedisCardBalanceServiceImpl cardService;

  @Autowired
  public RedisCardBalanceCT(RedisCardBalanceServiceImpl cardService) {
    this.cardService = cardService;
  }

  private CardBalance card;
  private Double startUpBalance = 1000.0;
  private String cardNumber = "1";

  @BeforeEach
  void beforeEach(){
    card = new CardBalance()
          .withBalance(startUpBalance)
          .withCardNumber(cardNumber);
    cardService.add(card);
  }

  @AfterEach
  void afterEach(){
    cardService.remove(card.getCardNumber());
  }

  //Find by card number
  @Test
  void findByCorrectCardNumber_ShouldReturn_OptionalOfCardBalance(){
      Assertions.assertEquals(Optional.of(card), cardService.findByNumber(cardNumber));
  }
  @Test
  void findByNullCardNumber_ShouldThrow_IllegalArgumentException(){
    Assertions.assertThrows(IllegalArgumentException.class,()-> cardService.findByNumber(null));
  }
  @Test
  void findByWrongCardNumber_ShouldReturnNull(){
    Assertions.assertEquals(cardService.findByNumber(card.getCardNumber()+100), Optional.empty());
  }
  //Add card

  @Test
  void addCardByCorrectData_ShouldReturn_Nothing_and_createCardBalance(){
    Assertions.assertEquals(Optional.of(card), cardService.findByNumber(card.getCardNumber()));
  }
  @Test
  void addCardBalanceByNullCardBalance_ShouldThrow_IllegalArgumentException(){
    Assertions.assertThrows(IllegalArgumentException.class,()-> cardService.add(null));
  }
  //Withdraw
  @Test
  void withdrawByCorrectData_Should_WithdrawMoneyFromCardBalance() throws CardOperationException {
    cardService.withdraw(card.getCardNumber(), card.getBalance()-1);
    Assertions.assertEquals(1, cardService.findByNumber(card.getCardNumber()).get().getBalance());
  }
  @Test
  void withdrawFromCardWithNotEnoughMoney_ShouldThrow_CardOperationException() throws CardOperationException {
    Assertions.assertThrows(CardOperationException.class, ()->cardService.withdraw(card.getCardNumber(),card.getBalance()+1));
  }
  @Test
  void withdrawByNotExistCard_ShouldThrow_IllegalArgumentException(){
    Assertions.assertThrows(IllegalArgumentException.class,()->cardService.withdraw(card.getCardNumber()+1000, card.getBalance()));
  }
  @Test
  void withdrawWithNegativeAmount_ShouldThrow_IllegalArgumentException(){
    Assertions.assertThrows(IllegalArgumentException.class,()-> cardService.withdraw(card.getCardNumber(),-10d));
  }

  //Replenish
  @Test
  void replenishWithCorrectData_Should_increaseCardBalance(){
    Double replenishAmount = 10d;
    cardService.replenish(card.getCardNumber(),replenishAmount);
    Assertions.assertEquals(cardService.findByNumber(card.getCardNumber()).get().getBalance(),startUpBalance+replenishAmount);
  }
  @Test
  void replenishWithNotExistCardNumber_ShouldThrow_IllegalArgumentException(){
    Assertions.assertThrows(IllegalArgumentException.class,()->cardService.replenish("a",100d));
  }
  @Test
  void replenishWithNegativeAmount_ShouldThrow_IllegalArgumentException(){
    Assertions.assertThrows(IllegalArgumentException.class,()-> cardService.replenish(card.getCardNumber(),-50d));
  }

  //Remove
  @Test
  void removeByCorrectData_Should_RemoveCardBalance(){
    String newCardNumber = "2";
    cardService.add(card.withCardNumber(newCardNumber));
    cardService.remove(newCardNumber);
    Assertions.assertTrue(cardService.findByNumber(newCardNumber).isEmpty());
  }
  @Test
  void removeByNullCardBalance_ShouldThrow_IllegalArgumentException(){
    Assertions.assertThrows(IllegalArgumentException.class,()-> cardService.remove(null));
  }
  @Test
  void removeByNotExistEntity_ShouldThrow_EntityNotFoundException(){
      Assertions.assertThrows(EntityNotFoundException.class,()->cardService.remove("100"));
  }
}
