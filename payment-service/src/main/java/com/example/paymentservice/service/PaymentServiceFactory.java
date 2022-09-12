package com.example.paymentservice.service;


import com.example.commondto.common.PaymentRequest;

public interface PaymentServiceFactory{
  PaymentService getPaymentService(PaymentRequest request);
}
