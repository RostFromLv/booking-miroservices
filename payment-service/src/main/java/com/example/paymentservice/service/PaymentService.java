package com.example.paymentservice.service;

import com.example.commondto.common.PaymentRequest;
import com.example.commondto.common.PaymentResponse;

public interface PaymentService {
  PaymentResponse doPayment(PaymentRequest request) throws CardOperationException;
}
