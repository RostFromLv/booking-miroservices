package com.example.ordersservice;

import com.example.commondto.common.OrderDto;
import com.example.commondto.common.Status;
import com.example.ordersservice.service.OrderRepository;
import com.example.ordersservice.service.OrderServiceImpl;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class OrderControllerV1IT {

  @LocalServerPort
  private int port;

  private final static Integer ID = 1;
  private final static Integer USER_ID = 2;
  private final static Integer HOTEL_ROOM_ID = 1;
  private final static Integer HOTEL_ID = 1;
  private final static Status STATUS = Status.OPEN;
  private final static Long FROM_DATE = 10L;
  private final static Long END_DATE = 1000L;
  private final static Long EXPIRED_AT = 9999999L;

  private final OrderServiceImpl orderService;
  private final OrderRepository orderRepository;

  @Autowired
  public OrderControllerV1IT(OrderServiceImpl orderService, OrderRepository orderRepository) {
    this.orderService = orderService;
    this.orderRepository = orderRepository;
  }
  @BeforeEach
  void beforeEach(){
    orderRepository.deleteAll();
    RestAssured.reset();
  }

  //Create
  @Test
  void createByCorrectDto_ShouldReturn_CreatedDto_and_Status201(){
    OrderDto createOrderDto = generateOrderDto();

    OrderDto actual = requestJson()
          .body(createOrderDto)
          .when()
          .post()
          .then()
          .statusCode(201)
          .extract()
          .as(OrderDto.class);

    Assertions.assertThat(actual)
          .usingRecursiveComparison()
          .ignoringFields("id","createdAt")
          .isEqualTo(createOrderDto);
  }
  //Get by id
  @Test
  void getByExistId_ShouldReturn_ExistOrderDto_and_Status200(){
    OrderDto existOrder = orderService.create(generateOrderDto());
    OrderDto actual =  request().when()
          .get("/"+existOrder.getId())
          .then()
          .statusCode(200)
          .extract()
          .as(OrderDto.class);

    Assertions.assertThat(actual).isEqualTo(existOrder);
  }
  @Test
  void getByWrongId_ShouldReturn_Status404(){
    request().when()
          .get("/"+ID)
          .then()
          .statusCode(404);
  }
  //Get all
  @Test
  void getAll_ShouldReturn_FullList_And_Status200(){
    orderService.create(generateOrderDto());
    orderService.create(generateOrderDto());
    request().when()
          .get()
          .then()
          .statusCode(200)
          .body("size()", Matchers.is(2));
  }
  @Test
  void getAll_ShouldReturn_EmptyList_And_Status200(){
      request().when()
            .get()
            .then()
            .statusCode(200)
            .body("size()",Matchers.is(0));
  }



  private OrderDto generateOrderDto() {
    OrderDto orderDto = new OrderDto();
    orderDto.setUserId(USER_ID);
    orderDto.setHotelRoomId(HOTEL_ROOM_ID);
    orderDto.setHotelId(HOTEL_ID);
    orderDto.setStatus(STATUS);
    orderDto.setFromDate(FROM_DATE);
    orderDto.setEndDate(END_DATE);
    orderDto.setExpiredAt(EXPIRED_AT);
    return orderDto;
  }
  private RequestSpecification request() {
    return RestAssured.given().port(port).basePath("/api/v1/orders");
  }

  private RequestSpecification requestJson() {
    return request().contentType(ContentType.JSON);
  }
}
