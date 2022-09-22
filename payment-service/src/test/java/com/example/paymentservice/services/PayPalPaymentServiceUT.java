package com.example.paymentservice.services;

import com.example.commondto.common.CardDto;
import com.example.commondto.common.PaymentRequest;
import com.example.commondto.common.PaymentResponse;
import com.example.paymentservice.service.CardBalanceService;
import com.example.paymentservice.service.CardOperationException;
import com.example.paymentservice.service.PayPalPaymentService;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PayPalPaymentServiceUT {
  private final static String cardNumber = "444";
  private final static String cardHolderName = "Petro Ivanochko";
  private final static String ccv2Code = "000";
  private final static Long expirationDate = 10000L;

  @Mock
  CardBalanceService cardBalance;

  @InjectMocks
  PayPalPaymentService paymentService;

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
  void doPaymentWithCorrectData_ShouldReturn_SuccessPayment() throws CardOperationException {

    paymentResponse.setSuccess(true);

    PaymentResponse actual = paymentService.doPayment(request);

    org.assertj.core.api.Assertions.assertThat(actual)
          .usingRecursiveComparison()
          .ignoringFields("transactionId", "timestamp")
          .isEqualTo(paymentResponse);
  }

  @Test
  void doPaymentWithExpiredCardDate_ShouldReturn_UnsuccessfulPayment_WithExpiredDateMessage() throws CardOperationException {
    request.getCardDto().setExpirationDate(50L);
    Assertions.assertThrows(CardOperationException.class,()->paymentService.doPayment(request));
  }

  @Test
  void doPaymentWithWrongCcv2Code_ShouldReturn_UnsuccessfulPayment_WithWrongCcv2CodeMessage() throws CardOperationException {
    request.getCardDto().setCcv2("999");
    Assertions.assertThrows(CardOperationException.class,()->paymentService.doPayment(request));
  }

  @Test
  void dPaymentWithExpiredDate_and_WrongCcv2Code_ShouldReturn_UnsuccessfulPayment_WithMultipleMessages() throws CardOperationException {
    request.getCardDto().setExpirationDate(100L);
    request.getCardDto().setCcv2("999");

    Assertions.assertThrows(CardOperationException.class,()->paymentService.doPayment(request));

  }
}
