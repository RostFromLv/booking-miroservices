package com.example.paymentservice.services;


import com.example.commondto.common.CardBalance;
import com.example.commondto.common.PaymentData;
import com.example.paymentservice.configuration.TestRedisConfiguration;
import com.example.paymentservice.service.RedisCardBalanceServiceImpl;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestRedisConfiguration.class)
public class RedisCardBalanceServiceV1IT {


  private final RedisCardBalanceServiceImpl cardService;

  @Autowired
  public RedisCardBalanceServiceV1IT(RedisCardBalanceServiceImpl cardService) {
    this.cardService = cardService;
  }

  @LocalServerPort
  private int port;

  private CardBalance card = new CardBalance();
  private Double startUpBalance = 1000.0;
  private String cardNumber = "1";
  PaymentData paymentData = new PaymentData();

  @BeforeEach
  void beforeEach() {
    card = new CardBalance()
          .withBalance(startUpBalance)
          .withCardNumber(cardNumber);
    cardService.add(card);
  }

  @AfterEach
  void afterEach() {
    cardService.remove(card.getCardNumber());
  }

  //Create
  @Test
  void addCardByCorrectData_Should_ReturnStatus201() {
    CardBalance newCardBalance = new CardBalance().withBalance(1000d).withCardNumber("10");

    requestJson()
          .body(newCardBalance)
          .when()
          .post()
          .then()
          .statusCode(201);

    Assertions.assertEquals(newCardBalance, cardService.findByNumber(newCardBalance.getCardNumber()).get());

    clearRedisBase(newCardBalance.getCardNumber());
  }

  //FindBy id
  @Test
  void findByCorrectNumber_ShouldReturn_OptionalOfExistCard_and_Status200() {
    CardBalance actual = request()
          .when()
          .get("/" + card.getCardNumber())
          .then()
          .statusCode(200)
          .extract()
          .as(CardBalance.class);

    Assertions.assertNotNull(actual);
    Assertions.assertEquals(card, actual);
  }

  @Test
  void findByWrongCardNumber_ShouldReturn_Status200_and_EmptyOptional() {

    request()
          .when()
          .get("/0")
          .then()
          .statusCode(404);

  }

  //Withdraw
  @Test
  void withdrawWithCorrectData_ShouldReturn_Status200_and_ReduceCardBalance() {
    PaymentData paymentData = new PaymentData().withPrice(card.getBalance() - 1).withCardNumber(card.getCardNumber());

    requestJson()
          .body(paymentData)
          .when()
          .put("/withdraw")
          .then()
          .statusCode(200);
    Assertions.assertEquals(cardService.findByNumber(card.getCardNumber()).get().getBalance(), 1);
  }

  @Test
  void withdrawWithNegativeAmount_ShouldReturn_Status400() {
    PaymentData paymentData = new PaymentData().withCardNumber(card.getCardNumber()).withPrice(-500d);

    requestJson().body(paymentData)
          .when()
          .put("/withdraw")
          .then()
          .statusCode(400);
  }

  @Test
  void withdrawWithNotEnoughCardBalance_ShouldReturn_Status400() {
    paymentData.withCardNumber(card.getCardNumber()).withPrice(card.getBalance() + 1);

    requestJson()
          .body(paymentData)
          .when()
          .put("/withdraw")
          .then()
          .statusCode(400);
  }

  @Test
  void withdrawWithNotExistCardNumber_ShouldReturn_Status404() {
    PaymentData paymentData = new PaymentData().withPrice(100d).withCardNumber("0");

    requestJson().body(paymentData)
          .when()
          .put("/withdraw")
          .then()
          .statusCode(400);
  }

  //Replenish
  @Test
  void replenishWithCorrectData_ShouldReturn_Status200_and_increaseCardBalance() {
    Double money = 100d;
    PaymentData paymentData = new PaymentData().withPrice(money).withCardNumber(card.getCardNumber());

    requestJson().body(paymentData)
          .when()
          .put("/replenish")
          .then()
          .statusCode(200);

    Assertions.assertEquals(cardService.findByNumber(card.getCardNumber()).get().getBalance(), startUpBalance + money);
  }

  @Test
  void replenishWithNegativeAmount_ShouldReturn_Status400() {
    paymentData.withPrice(-500d).withCardNumber(card.getCardNumber());

    requestJson().body(paymentData)
          .when()
          .put("/replenish")
          .then()
          .statusCode(400);
  }

  @Test
  void replenishWithNotExistCard_ShouldReturn_Status404() {
    PaymentData paymentData = new PaymentData().withPrice(100d).withCardNumber("0");

    requestJson().body(paymentData)
          .when()
          .put("/replenish")
          .then()
          .statusCode(400);
  }

  //Remove
  @Test
  void removeByCorrectData_ShouldReturn_Status201() {

    request()
          .when()
          .delete("/" + card.getCardNumber())
          .then()
          .statusCode(204);

    cardService.add(card);
  }

  @Test
  void removeByNotExistCard_ShouldReturn_Status404() {
    request()
          .when()
          .delete("/2")
          .then()
          .statusCode(404);
  }


  private RequestSpecification request() {
    return RestAssured.given().port(port).basePath("/api/v1/payments/card");
  }

  private RequestSpecification requestJson() {
    return request().contentType(ContentType.JSON);
  }

  private void clearRedisBase(String cardNumber) {
    cardService.remove(cardNumber);
  }
}
