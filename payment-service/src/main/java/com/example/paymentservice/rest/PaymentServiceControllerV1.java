package com.example.paymentservice.rest;

import com.example.commondto.common.CardBalance;
import com.example.commondto.common.PaymentData;
import com.example.commondto.common.PaymentRequest;
import com.example.commondto.common.PaymentResponse;
import com.example.paymentservice.service.CardBalanceService;
import com.example.paymentservice.service.CardOperationException;
import com.example.paymentservice.service.PaymentExecutor;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentServiceControllerV1 {

  private  final PaymentExecutor executor;
  private final CardBalanceService cardBalanceService;

  @Autowired
  public PaymentServiceControllerV1(PaymentExecutor executor, CardBalanceService cardBalanceService) {
    this.executor = executor;
    this.cardBalanceService = cardBalanceService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public PaymentResponse doPayment(@RequestBody @NotNull  PaymentRequest request) throws
      CardOperationException {
    return executor.doPayment(request);
  }

  //Card related operations
  @PostMapping("/card")
  @ResponseStatus(HttpStatus.CREATED)
  public void createCard(@RequestBody CardBalance cardBalance) {
    cardBalanceService.add(cardBalance);
  }

  @PutMapping("/card/withdraw")
  @ResponseStatus(HttpStatus.OK)
  public void withdraw(@RequestBody PaymentData data) throws CardOperationException {
    cardBalanceService.withdraw(data.getCardNumber(), data.getPrice());
  }

  @PutMapping("/card/replenish")
  @ResponseStatus(HttpStatus.OK)
  public void replenish(@RequestBody PaymentData data )  {
    cardBalanceService.replenish(data.getCardNumber(), data.getPrice());
  }

  @GetMapping("/card/{cardNumber}")
  @ResponseStatus(HttpStatus.OK)
  public CardBalance findByNumber(@PathVariable String cardNumber){
    return cardBalanceService.findByNumber(cardNumber).get();
  }

  @DeleteMapping("/card/{cardNumber}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable String cardNumber){
    cardBalanceService.remove(cardNumber);
  }

}
