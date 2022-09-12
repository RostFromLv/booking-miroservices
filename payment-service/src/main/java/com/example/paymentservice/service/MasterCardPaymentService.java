package com.example.paymentservice.service;

import com.example.commondto.common.CardDto;
import com.example.commondto.common.PaymentRequest;
import com.example.commondto.common.PaymentResponse;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MasterCardPaymentService implements PaymentService {
  private  final CardBalanceService cardBalanceService;

  @Autowired
  MasterCardPaymentService(CardBalanceService cardBalanceService) {
    this.cardBalanceService = cardBalanceService;
  }

  @Override
  public PaymentResponse doPayment(PaymentRequest request)  {
    PaymentResponse response =
        new PaymentResponse();
    CardDto card = request.getCardDto();

    if (card.getExpirationDate()<500){
      response.addError("Your card expired");
    }
    if (!card.getCcv2().equals("123")){
      response.addError("Wrong ccv2 code");
    }
    try {
      cardBalanceService.withdraw(card.getNumber(), request.getPrice());
    } catch (CardOperationException e) {
      response.addError(e.getMessage());
    }
    response.setSuccess(!response.hasError());
    response.setTimestamp(System.currentTimeMillis());
    response.setTransactionId(UUID.randomUUID());
    log.trace("VISA| Withdraw transaction by card "+ card.getNumber());
    return response;
  }
}
