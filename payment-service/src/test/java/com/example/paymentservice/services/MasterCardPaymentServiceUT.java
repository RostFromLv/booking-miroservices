package com.example.paymentservice.services;

import com.example.commondto.common.CardDto;
import com.example.commondto.common.PaymentRequest;
import com.example.commondto.common.PaymentResponse;
import com.example.paymentservice.service.MasterCardPaymentService;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MasterCardPaymentServiceUT {
  private final static String cardNumber = "800";
  private final static String cardHolderName = "Petro Ivanochko";
  private final static String ccv2Code = "123";
  private final static Long expirationDate = 1000L;

  @InjectMocks
  MasterCardPaymentService paymentService;

  CardDto existCard = new CardDto();
  PaymentRequest request = new PaymentRequest();
  PaymentResponse paymentResponse = new PaymentResponse();

  @BeforeEach
  void beforeEach() {
    existCard.setNumber(cardNumber);
    existCard.setExpirationDate(expirationDate);
    existCard.setHolderFullName(cardHolderName);
    existCard.setCcv2(ccv2Code);

    request.setCardDto(existCard);
    request.setPrice(2000d);

    paymentResponse.setTimestamp(System.currentTimeMillis());
    paymentResponse.setTransactionId(UUID.randomUUID());
  }

  @Test
  void doPaymentWithCorrectData_ShouldReturn_SuccessPayment() {

    paymentResponse.setSuccess(true);

    PaymentResponse actual = paymentService.doPayment(request);

    Assertions.assertThat(actual)
          .usingRecursiveComparison()
          .ignoringFields("transactionId", "timestamp")
          .isEqualTo(paymentResponse);
  }

  @Test
  void doPaymentWithExpiredCardDate_ShouldReturn_UnsuccessfulPayment_WithExpiredDateMessage() {
    request.getCardDto().setExpirationDate(50L);

    PaymentResponse actual = paymentService.doPayment(request);

    org.junit.jupiter.api.Assertions.assertTrue(actual.getErrors().size() == 1);
    org.junit.jupiter.api.Assertions.assertTrue(actual.getSuccess().equals(false));
  }

  @Test
  void doPaymentWithWrongCcv2Code_ShouldReturn_UnsuccessfulPayment_WithWrongCcv2CodeMessage() {
    request.getCardDto().setCcv2("999");
    PaymentResponse actual = paymentService.doPayment(request);
    org.junit.jupiter.api.Assertions.assertTrue(actual.getSuccess().equals(false));
    org.junit.jupiter.api.Assertions.assertTrue(actual.getErrors().size() == 1);
  }

  @Test
  void dPaymentWithExpiredDate_and_WrongCcv2Code_ShouldReturn_UnsuccessfulPayment_WithMultipleMessages() {
    request.getCardDto().setExpirationDate(100L);
    request.getCardDto().setCcv2("999");

    PaymentResponse actual = paymentService.doPayment(request);

    org.junit.jupiter.api.Assertions.assertTrue(actual.getSuccess().equals(false));
    org.junit.jupiter.api.Assertions.assertTrue(actual.getErrors().size() == 2);

  }
}
