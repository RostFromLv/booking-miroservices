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
  private final static String cardOperationException = "Exception in MasterCardPaymentService in withdraw operation";

  @Autowired
  MasterCardPaymentService(CardBalanceService cardBalanceService) {
    this.cardBalanceService = cardBalanceService;
  }

  @Override
  public PaymentResponse doPayment(PaymentRequest request) throws CardOperationException {
    PaymentResponse response = new PaymentResponse();
    CardDto card = request.getCardDto();

    if (card.getExpirationDate()<500){
      throw new CardOperationException("Your card expired");
    }
    if (!card.getCcv2().equals("123")){
      throw new CardOperationException("Wrong ccv2 code");
    }
    try {
      cardBalanceService.withdraw(card.getNumber(), request.getPrice());
    } catch (CardOperationException e) {
      throw new CardOperationException(cardOperationException);
    }
    response.setSuccess(true);
    response.setTimestamp(System.currentTimeMillis());
    response.setTransactionId(UUID.randomUUID());
    log.trace("VISA| Withdraw transaction by card "+ card.getNumber());
    return response;
  }
}
