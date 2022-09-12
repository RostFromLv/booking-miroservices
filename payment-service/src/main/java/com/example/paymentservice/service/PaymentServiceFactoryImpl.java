package com.example.paymentservice.service;

import com.example.commondto.common.PaymentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceFactoryImpl implements PaymentServiceFactory {
  private final CardBalanceService cardBalanceService;

  @Autowired
  public PaymentServiceFactoryImpl(CardBalanceService cardBalanceService) {
    this.cardBalanceService = cardBalanceService;
  }

  @Override
  public PaymentService getPaymentService(PaymentRequest request) {
    String cardNumber = request.getCardDto().getNumber();
    if (cardNumber.startsWith("1")) {
      return new VisaPaymentService(cardBalanceService);
    } else if (cardNumber.startsWith("4")) {
      return new PayPalPaymentService(cardBalanceService);
    } else if (cardNumber.startsWith("8")) {
      return new MasterCardPaymentService(cardBalanceService);
    }
    return null;
  }
}
