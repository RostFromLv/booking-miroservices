package com.example.addressservice;


import com.example.addressservice.model.AddressDto;
import com.example.addressservice.service.AddressServiceImpl;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import javax.validation.constraints.NotNull;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;


@ActiveProfiles("integrationTest")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AddressControllerV1IT {

  private final AddressServiceImpl addressService;

  @LocalServerPort
  private int port;

  private final Integer NOT_EXIST_ID = 1000;

  @Autowired
  public AddressControllerV1IT(AddressServiceImpl addressService) {
    this.addressService = addressService;
  }


  @AfterEach
  void afterEach() {
    addressService.deleteAll();
    RestAssured.reset();
  }

  //Delete
  @Test
  void deleteByCorrectId_ShouldReturn_Status204() {
    AddressDto address = new AddressDto();
    address.setCity("City");
    address.setStreet("Street");
    address.setCountry("Country");
    address.setHouseNumber(15);
    address.setPostalCode("postal_code");

    AddressDto savedAddress = this.addressService.create(address);
    Integer addressId = savedAddress.getId();

    Assertions.assertNotNull(addressId);
    Assertions.assertTrue( this.addressService.findById(addressId).isPresent());

    this.request()
        .when()
        .delete(addressId.toString())
        .then()
        .assertThat().statusCode(204);
  }

  @Test
  void deleteByIncorrectId_ShouldThrow_EmptyResultDataAccessException() {
    request()
        .when()
        .delete(NOT_EXIST_ID.toString())
        .then()
        .assertThat()
        .statusCode(404);
  }

  //CREATE
  @Test
  void createByCorrectDto_ShouldReturn_CreatedDto() {
    String country = "Ukraine";

    AddressDto addressDto = requestJson()
        .and()
        .body(generateDto(country))
        .when()
        .post()
        .then()
        .statusCode(201)
        .extract()
        .as(AddressDto.class);

    Assertions.assertEquals(country, addressDto.getCountry());
  }

  @Test
  void createByWrongDtoArguments_ShouldThrow_DataIntegrityViolationException() {
    requestJson()
        .and()
        .body(generateDto("Ukraine").toString())
        .when()
        .post()
        .then()
        .assertThat()
        .statusCode(400);
  }

  //UPDATE
  @Test
  void updateByCorrectDto_ShouldReturn_UpdatedDto() {
    String country = "Ukraine";
    AddressDto createdAddress = addressService.create(generateDto("Poland"));

    createdAddress.setCountry(country);

    AddressDto addressDto = requestJson()
        .and()
        .body(createdAddress)
        .when()
        .put()
        .then()
        .statusCode(200)
        .extract()
        .as(AddressDto.class);

    Assertions.assertEquals(country, addressDto.getCountry());
    Assertions.assertEquals(createdAddress.getId(), addressDto.getId());
  }

  @Test
  void updateByWrongDto_ShouldThrow_DataIntegrityViolationException() {

    requestJson()
        .and()
        .body(generateDto("Ukraine").toString())
        .when()
        .put()
        .then()
        .assertThat()
        .statusCode(400);
  }

  @Test
  void updateByConstraintViolation_ShouldThrow_ConstraintViolationException() {
    addressService.create(generateDto("Country"));

    requestJson()
        .and()
        .body(generateDto("Ukraine").toString())
        .when()
        .put()
        .then()
        .assertThat()
        .statusCode(400);
  }

  //GetAll
  @Test
  void getAll_ShouldReturn_EmptyList() {
    request()
        .get()
        .then()
        .assertThat()
        .statusCode(200)
        .and()
        .assertThat()
        .body("size()", Matchers.is(0));
  }

  @Test
  void getAll_ShouldReturn_ListOfAddressDto() {
    addressService.create(generateDto("Ukraine"));
    addressService.create(generateDto("Poland"));
    addressService.create(generateDto("France"));

    request()
        .get()
        .then()
        .assertThat()
        .statusCode(200)
        .and()
        .assertThat()
        .body("size()", Matchers.is(3));
  }

  //Get By Id
  @Test
  void getByCorrectId_ShouldReturn_Dto() {
    AddressDto createdDto = addressService.create(generateDto("Ukraine"));

        AddressDto addressDto = request()
        .get(createdDto.getId().toString())
        .then()
        .statusCode(200)
        .extract()
        .as(AddressDto.class);

    Assertions.assertEquals(1, addressDto.getId());
    Assertions.assertEquals("Ukraine", addressDto.getCountry());
  }

  @Test
  void getByWrongId_ShouldTrow_EntityNotExistException() {
    request()
        .get(NOT_EXIST_ID.toString())
        .then()
        .assertThat()
        .statusCode(404);
  }

  @Test
  void getByIncorrectArgument_ShouldThrow_NumberFormatException() {
    request()
        .get("bad")
        .then()
        .assertThat()
        .statusCode(400);
  }

  /**
   * Use this method for create different Dto.
   * <p>
   * Parameter is for creating different Dto because of constraint
   *
   * @param country
   * @return fullfilled {@link AddressDto}
   */
  public AddressDto generateDto(String country) {
    AddressDto addressDto = new AddressDto();
    addressDto.setCountry(country);
    addressDto.setCity("City");
    addressDto.setStreet("Street");
    addressDto.setHouseNumber(1);
    addressDto.setPostalCode("778954");
    return addressDto;
  }

  private RequestSpecification request() {
    return RestAssured.given()
        .port(this.port)
        .basePath("/api/v1/addresses");
  }

  private RequestSpecification requestJson() {
    return request().contentType(ContentType.JSON);
  }
}
