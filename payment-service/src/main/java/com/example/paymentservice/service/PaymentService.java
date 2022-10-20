package com.example.paymentservice.service;

import com.example.commondto.common.PaymentRequest;
import com.example.commondto.common.PaymentResponse;
import javax.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface PaymentService {
  @Nullable PaymentResponse doPayment(@NotNull PaymentRequest request) throws CardOperationException;
}
