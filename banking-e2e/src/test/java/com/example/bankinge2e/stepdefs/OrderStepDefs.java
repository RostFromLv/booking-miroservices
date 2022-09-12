package com.example.bankinge2e.stepdefs;

import com.example.bankinge2e.feign.AddressFeignClient;
import com.example.bankinge2e.feign.HotelFeignClient;
import com.example.bankinge2e.feign.OrderServiceClient;
import com.example.bankinge2e.feign.PaymentServiceClient;
import com.example.bankinge2e.feign.UserFeignClient;
import com.example.commondto.common.AddressDto;
import com.example.commondto.common.CardBalance;
import com.example.commondto.common.CardDto;
import com.example.commondto.common.HotelDto;
import com.example.commondto.common.HotelRoomDto;
import com.example.commondto.common.OrderDto;
import com.example.commondto.common.PaymentData;
import com.example.commondto.common.RoomPriceDto;
import com.example.commondto.common.Status;
import com.example.commondto.common.UserDto;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.But;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderStepDefs {

  private HotelDto createdHotel;
  private UserDto createdUser;
  private HotelRoomDto createdRoom;
  private CardBalance createdCardBalance;
  private OrderDto createdOrder;
  private AddressDto createdAddress;
  private RoomPriceDto createdRoomPrice;

  protected final AddressFeignClient addressFeignClient;
  protected final HotelFeignClient hotelFeignClient;
  protected final PaymentServiceClient paymentServiceClient;
  protected final UserFeignClient userFeignClient;
  protected final OrderServiceClient orderServiceClient;

  public OrderStepDefs(AddressFeignClient addressFeignClient, HotelFeignClient hotelFeignClient, PaymentServiceClient paymentServiceClient, UserFeignClient userFeignClient, OrderServiceClient orderServiceClient) {
    this.addressFeignClient = addressFeignClient;
    this.hotelFeignClient = hotelFeignClient;
    this.paymentServiceClient = paymentServiceClient;
    this.userFeignClient = userFeignClient;
    this.orderServiceClient = orderServiceClient;
  }

  @After
  public void after(){
    addressFeignClient.deleteAll();
    hotelFeignClient.deleteAllHotels();
    hotelFeignClient.deleteAllPrices();
    hotelFeignClient.deleteAllRooms();
    hotelFeignClient.deleteAllReservations();
    paymentServiceClient.delete(createdCardBalance.getCardNumber());
    userFeignClient.deleteAllUsers();
    orderServiceClient.deleteAllOrders();
  }

  //Support methods
  private OrderDto initializeOrder(Long fromDate, Long endDate, Integer userId) {
    CardDto cardDto = new CardDto();
    cardDto.setNumber(createdCardBalance.getCardNumber());
    cardDto.setExpirationDate(1740L);
    cardDto.setCcv2("555");
    cardDto.setHolderFullName(createdUser.getFirstName()+" "+createdUser.getLastName());

    OrderDto orderDto = new OrderDto();
    orderDto.setFromDate(fromDate);
    orderDto.setEndDate(endDate);
    orderDto.setCard(cardDto);
    orderDto.setUserId(userId);
    orderDto.setHotelId(createdRoom.getHotelId());
    orderDto.setHotelRoomId(createdRoom.getId());
    orderDto.setCreatedAt(10L);
    orderDto.setExpiredAt(1749558196000L);
    orderDto.setStatus(Status.OPEN);
    return orderDto;
  }

  //Third scenario
  @Given("address in city {string} on street {string} {string}")
  public void inCityOnStreet(String cityName, String street, String houseNumber) {
    AddressDto addressDto = new AddressDto();
    addressDto.setId(1000);
    addressDto.setCountry("Ukraine");
    addressDto.setCity(cityName);
    addressDto.setStreet(street);
    addressDto.setHouseNumber(Integer.parseInt(houseNumber));
    addressDto.setPostalCode("postalCode");

    createdAddress =  addressFeignClient.create(addressDto);

    Assertions.assertNotNull(createdAddress);
    org.assertj.core.api.Assertions.assertThat(createdAddress)
          .usingRecursiveComparison()
          .ignoringFields("id")
          .isEqualTo(addressDto);
  }

  @And("system create hotel with name {string}")
  public void systemCreateHotelWithName(String hotelName) {
    HotelDto hotelDto = new HotelDto();
    hotelDto.setName(hotelName);
    hotelDto.setAddressId(createdAddress.getId());
    createdHotel = hotelFeignClient.createHotel(hotelDto);

    Assertions.assertNotNull(createdHotel);
    org.assertj.core.api.Assertions.assertThat(createdHotel)
          .usingRecursiveComparison()
          .ignoringFields("id")
          .isEqualTo(hotelDto);
  }

  @Given("price {string} dollars per day for room with type {string}")
  public void priceDollarsPerDayForRoomWithType(String roomPrice, String roomType) {
    RoomPriceDto roomPriceDto = new RoomPriceDto();
    roomPriceDto.setId(1000);
    roomPriceDto.setPrice(Double.parseDouble(roomPrice));
    roomPriceDto.setDefaultPrice(Double.parseDouble(roomPrice));
    roomPriceDto.setStartDate(10L);
    roomPriceDto.setEndDate(System.currentTimeMillis());
    createdRoomPrice =  hotelFeignClient.createRoomPrice(roomPriceDto);

    Assertions.assertNotNull(roomPrice);
    org.assertj.core.api.Assertions.assertThat(createdRoomPrice)
          .usingRecursiveComparison()
          .ignoringFields("id")
          .isEqualTo(roomPriceDto);

    HotelRoomDto hotelRoomDto = new HotelRoomDto();
    hotelRoomDto.setHotelId(createdHotel.getId());
    hotelRoomDto.setRoomPriceId(createdRoomPrice.getId());
    hotelRoomDto.setArea(150f);
    hotelRoomDto.setType(roomType);
    createdRoom = hotelFeignClient.createRoom(hotelRoomDto);

    Assertions.assertNotNull(createdRoom);
    org.assertj.core.api.Assertions.assertThat(createdRoom)
          .usingRecursiveComparison()
          .ignoringFields("id")
          .isEqualTo(hotelRoomDto);

  }

  @Given("system create user with email {string} and name {string}")
  public void systemCreateUserWithEmailAndName(String userEmail, String userName) {
    UserDto userDto = new UserDto();
    userDto.setId(1000);
    userDto.setFirstName(userName);
    userDto.setEmail(userEmail);
    userDto.setLastName("Petrenko");
    userDto.setPhoneNumber("+380 63 25 45 888");
    userDto.setCreatedAt(1l);
    userDto.setUpdatedAt(10l);
    createdUser = userFeignClient.create(userDto);

    Assertions.assertNotNull(createdUser);
    org.assertj.core.api.Assertions.assertThat(createdUser)
          .usingRecursiveComparison()
          .ignoringFields("id","createdAt","updatedAt")
          .isEqualTo(userDto);
  }

  @When("{string} try to book room from {string} to {string}")
  public void tryToBookRoomFromTo(String userName, String startDate, String endDate) {
    Assertions.assertEquals(createdUser.getFirstName(), userName);

    OrderDto orderDto  = initializeOrder(Long.parseLong(startDate),Long.parseLong(endDate), createdUser.getId());
    createdOrder = orderServiceClient.doPayment(orderDto);

    Assertions.assertNotNull(createdOrder);
  }

  @When("{string} replenished his card balance of {string} dollars")
  public void replenishedHisCardBalanceOfDollars(String userName, String replenishPrice) {
    Double priceForReplenish = Double.parseDouble(replenishPrice);
    Double currentBalance = createdCardBalance.getBalance();
    Assertions.assertEquals(createdUser.getFirstName(), userName);
    PaymentData paymentData = new PaymentData();
    paymentData.setCardNumber(createdCardBalance.getCardNumber());
    paymentData.setPrice(priceForReplenish);

    paymentServiceClient.replenish(paymentData);
    CardBalance updatedCardBalance = paymentServiceClient.findByNumber(createdCardBalance.getCardNumber());
    Assertions.assertEquals(updatedCardBalance.getBalance(),currentBalance+priceForReplenish);
  }

  @But("order has status {string}")
  public void orderHasStatus(String status) {
    Assertions.assertEquals(createdOrder.getStatus().toString(),status);
  }


  @Then("{string} try to book room from {string} to {string} dates again")
  public void tryToBookRoomFromToDatesAgain(String userName, String startDate, String endDate ) {

    Assertions.assertEquals(createdUser.getFirstName(), userName);

    OrderDto orderDto  = initializeOrder(Long.parseLong(startDate),Long.parseLong(endDate), createdUser.getId());
    createdOrder = orderServiceClient.doPayment(orderDto);

    Assertions.assertNotNull(createdOrder);
  }


  @And("user {string} has card number {string} with balance of {string} dollars")
  public void userHasCardNumberWithBalanceOfDollars(String userName, String cardNumber, String cardSum) {
    Assertions.assertEquals(createdUser.getFirstName(),userName);
    CardBalance cardBalance = new CardBalance();
    cardBalance.setCardNumber(cardNumber);
    cardBalance.setBalance(Double.parseDouble(cardSum));
    paymentServiceClient.createCard(cardBalance);

    Assertions.assertNotNull(paymentServiceClient.findByNumber(cardNumber));

    createdCardBalance = cardBalance;
  }

  @Then("expect order has status become {string}")
  public void expectOrderHasStatusBecome(String status) {
    Assertions.assertEquals(createdOrder.getStatus().toString(),status);
  }

}
