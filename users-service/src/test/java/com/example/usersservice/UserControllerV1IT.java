package com.example.usersservice;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.commondto.common.UserDto;
import com.example.usersservice.service.UserRepository;
import com.example.usersservice.service.UserServiceImpl;
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

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerV1IT {

  private final static int ID = 1000;
  private final static String FIRST_NAME = "First_Name";
  private final static String LAST_NAME = "Last_Name";
  private final static String EMAIL = "email@w.com";
  private final static String PHONE_NUMBER = "123456789";

  private final UserServiceImpl userService;
  private final UserRepository userRepository;

  @LocalServerPort
  private int port;

  @Autowired
  public UserControllerV1IT(UserServiceImpl userService, UserRepository userRepository) {
    this.userService = userService;
    this.userRepository = userRepository;
  }


  @BeforeEach
  void beforeEach() {
    userRepository.deleteAll();
    RestAssured.reset();
  }

  //Create
  @Test
  void createByCorrectDto_ShouldReturn_CreatedUserDto_AND_Status_201() {
    UserDto dtoForCreating = generateCustomDto(ID, FIRST_NAME, EMAIL);
    UserDto userDto = requestJson()
          .body(dtoForCreating)
          .when()
          .post()
          .then()
          .statusCode(201)
          .extract()
          .as(UserDto.class);

    Assertions.assertThat(userDto)
          .usingRecursiveComparison()
          .ignoringFields("id", "createdAt", "updatedAt")
          .isEqualTo(dtoForCreating);
  }

  @Test
  void createByDtoWithSameMail_ShouldThrow_EntityExistException() {
    userService.create(generateCustomDto(ID, FIRST_NAME, EMAIL));

    requestJson()
          .body(generateCustomDto(ID, "NewName", EMAIL))
          .when()
          .post()
          .then()
          .statusCode(409);

  }

  //Update
  @Test
  void updateByCorrectDto_ShouldReturn_UpdatedDto_AND_Status_200() {
    UserDto userDto = userService.create(generateCustomDto(ID, FIRST_NAME, EMAIL));
    UserDto userDtoForUpdate = generateCustomDto(userDto.getId(), "UpdatedName", "updated@email");

    UserDto updatedDto = requestJson()
          .body(generateCustomDto(userDto.getId(), "UpdatedName", "updated@email"))
          .when()
          .put()
          .then()
          .statusCode(200)
          .extract()
          .as(UserDto.class);
    Assertions.assertThat(updatedDto)
          .usingRecursiveComparison()
          .ignoringFields("createdAt", "updatedAt")
          .isEqualTo(userDtoForUpdate);
  }

  @Test
  void updateByNullId_ShouldThrow_IllegalArgumentException() {
    requestJson()
          .body(generateCustomDto(ID, FIRST_NAME, EMAIL))
          .when()
          .put()
          .then()
          .statusCode(400);
  }



  @Test
  void updateByDtoWithSameEmail_ShouldThrow_EntityExistException() {
    userService.create(generateCustomDto(ID, FIRST_NAME, EMAIL));
    UserDto secondUserDto = userService.create(generateCustomDto(ID, "SecondName", "sec@mail"));

    UserDto userDtoForUpdate = generateCustomDto(secondUserDto.getId(), "newName", EMAIL);

    requestJson()
          .body(userDtoForUpdate)
          .when()
          .put()
          .then()
          .statusCode(409);
  }

  //Get all
  @Test
  void getAll_ShouldReturn_EmptyList() {
    request()
          .get()
          .then()
          .statusCode(200)
          .and()
          .assertThat()
          .body("size()", Matchers.is(0));
  }

  @Test
  void getAll_ShouldReturn_FullList() {
    userService.create(generateCustomDto(ID, FIRST_NAME, EMAIL));
    userService.create(generateCustomDto(ID, "SecondName", "second@mail"));
    userService.create(generateCustomDto(ID, "thirdName", "third@mail"));

    request()
          .get()
          .then()
          .statusCode(200)
          .assertThat()
          .assertThat()
          .body("size()", Matchers.is(3));
  }

  //Get by id
  @Test
  void getByCorrectId_ShouldReturn_ExistDto() {
    UserDto userDto = userService.create(generateCustomDto(ID, FIRST_NAME, EMAIL));

    UserDto existDto = request()
          .get("/" + userDto.getId())
          .then()
          .statusCode(200)
          .extract()
          .as(UserDto.class);

    assertEquals(userDto, existDto);
  }

  @Test
  void getByWrongId_ShouldReturn_EntityNotFoundException() {
    request()
          .get("/1")
          .then()
          .statusCode(404);
  }

  //Delete by id
  @Test
  void deleteByCorrectId_ShouldReturn_Status_204() {
    UserDto userDto = userService.create(generateCustomDto(ID, FIRST_NAME, EMAIL));
    request()
          .delete("/" + userDto.getId())
          .then()
          .statusCode(204);
  }

  @Test
  void deleteByWrongId_ShouldThrow_EntityNotFoundException() {
    request()
          .delete("/1")
          .then()
          .statusCode(404);
  }


  private UserDto generateDto() {
    UserDto userDto = new UserDto();
    userDto.setId(ID);
    userDto.setFirstName(FIRST_NAME);
    userDto.setEmail(EMAIL);
    userDto.setLastName(LAST_NAME);
    userDto.setPhoneNumber(PHONE_NUMBER);
    return userDto;
  }

  private UserDto generateCustomDto(Integer id, String firstName, String email) {
    UserDto userDto = generateDto();
    userDto.setId(id);
    userDto.setEmail(email);
    userDto.setFirstName(firstName);
    return userDto;
  }

  private RequestSpecification request() {
    return RestAssured.given().port(port).basePath("/api/v1/users");
  }

  private RequestSpecification requestJson() {
    return request().contentType(ContentType.JSON);
  }
}
