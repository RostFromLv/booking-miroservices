package com.example.paymentservice.service;

import com.example.commondto.common.PaymentRequest;
import com.example.commondto.common.PaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FactoryPaymentExecutor implements PaymentExecutor{

  private final PaymentServiceFactory serviceFactory;

  @Autowired
  public FactoryPaymentExecutor(PaymentServiceFactory serviceFactory) {
    this.serviceFactory = serviceFactory;
  }

  @Override
  public PaymentResponse doPayment(PaymentRequest request) throws CardOperationException {
    return serviceFactory.getPaymentService(request).doPayment(request);
  }
}
