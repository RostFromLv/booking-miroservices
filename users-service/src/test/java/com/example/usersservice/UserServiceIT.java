package com.example.usersservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.commondto.common.UserDto;
import com.example.usersservice.service.UserRepository;
import com.example.usersservice.service.UserServiceImpl;
import javax.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceIT {

  private static int ID = 1000;
  private static String FIRST_NAME = "FIRST_NAME";
  private static String LAST_NAME = "LAST_NAME";
  private static String PHONE_NUMBER = "123456789";
  private static String EMAIL = "mail.com";

  private final UserServiceImpl userService;
  private final UserRepository userRepository;


  @Autowired
  public UserServiceIT(UserServiceImpl userService, UserRepository userRepository) {
    this.userService = userService;
    this.userRepository = userRepository;
  }

  @BeforeEach
  void beforeEach() {
    userRepository.deleteAll();
  }

  //Create
  @Test
  void createByCorrectDto_ShouldReturn_CreatedDto() {
    UserDto userDtoForCreate = generateCustomDto(ID, FIRST_NAME, EMAIL);

    UserDto createdUser = userService.create(userDtoForCreate);
    org.junit.jupiter.api.Assertions.assertNotNull(createdUser.getId());

    Assertions.assertThat(userDtoForCreate)
          .usingRecursiveComparison()
          .ignoringFields("id", "createdAt", "updatedAt")
          .isEqualTo(createdUser);
  }

  @Test
  void createByExistEmail_ShouldThrow_EntityExistException() {
    userService.create(generateCustomDto(ID, FIRST_NAME, EMAIL));
    assertThrows(DataIntegrityViolationException.class,
          () -> userService.create(generateCustomDto(ID, "OtherName", EMAIL)));
  }

  @Test
  void createByConstraintViolation_ShouldThrow_DataIntegrityViolationException() {
    userService.create(generateCustomDto(ID, FIRST_NAME, EMAIL));

    assertThrows(DataIntegrityViolationException.class,
          () -> userService.create(generateCustomDto(ID, FIRST_NAME, "new@mail.com")));
  }

  //Update


  @Test
  void updateByNotExistId_ShouldThrow_EntityNotFoundException() {
    EntityNotFoundException actual = assertThrows(EntityNotFoundException.class, () -> userService.update(generateDto(),ID));
    assertInstanceOf(EntityNotFoundException.class, actual);
  }

  @Test
  void updateByConstraintViolation_ShouldThrow_DataIntegrityViolationException() {
    userService.create(generateCustomDto(ID, FIRST_NAME, EMAIL));
    UserDto secondUserDto = userService.create(generateCustomDto(ID, "SecondName", "second@mail"));

    UserDto userDtoForUpdate = generateCustomDto(secondUserDto.getId(), FIRST_NAME, "third@mail.com");

     assertThrows(DataIntegrityViolationException.class,
          () -> userService.update(userDtoForUpdate,userDtoForUpdate.getId()));
  }

  @Test
  void updateWithExistEmail_ShouldThrow_EntityExistException() {
    userService.create(generateCustomDto(ID, FIRST_NAME, EMAIL));
    UserDto secondUserDto = userService.create(generateCustomDto(ID, "SecondName", "second@mail"));

    UserDto userDtoForUpdate = generateCustomDto(secondUserDto.getId(), "Name", EMAIL);

    assertThrows(DataIntegrityViolationException.class, () -> userService.update(userDtoForUpdate,userDtoForUpdate.getId()));
  }

  @Test
  void updateByCorrectDto_ShouldReturn_UpdatedDto() {
    UserDto createdUserDto = userService.create(generateCustomDto(ID, FIRST_NAME, EMAIL));
    UserDto userDtoForUpdate = generateCustomDto(createdUserDto.getId(), "Pet", "any@mail.com");

    UserDto actual = userService.update(userDtoForUpdate, userDtoForUpdate.getId());
    Assertions.assertThat(userDtoForUpdate)
          .usingRecursiveComparison()
          .ignoringFields("createdAt", "updatedAt")
          .isEqualTo(actual);
  }

  //Get by id
  @Test
  void getByExistId_ShouldReturn_ExistDto() {
    UserDto userDto = userService.create(generateCustomDto(ID, FIRST_NAME, EMAIL));

    UserDto actual = userService.findById(userDto.getId()).get();

    assertEquals(userDto, actual);
  }

  @Test
  void getByNotExistId_ShouldThrow_EntityNotFoundException() {
    assertThrows(EntityNotFoundException.class, () -> userService.findById(ID).get());
  }

  @Test
  void getAll_ShouldReturn_EmptyList() {
    assertEquals(userService.findAll().size(), 0);
  }

  @Test
  void getAll_ShouldReturn_FullList() {
    userService.create(generateCustomDto(ID, FIRST_NAME, EMAIL));
    userService.create(generateCustomDto(ID, "NEXT_NAME", "next@mail"));
    assertEquals(userService.findAll().size(), 2);
  }

  //Delete
  @Test
  void deleteByExistId_ShouldReturn_DifferentSize() {
    UserDto userDto = userService.create(generateCustomDto(ID, FIRST_NAME, EMAIL));
    userRepository.deleteById(userDto.getId());
    assertFalse(userRepository.existsById(userDto.getId()));
  }

  @Test
  void deleteByWrongId_ShouldThrow_EmptyResultDataAccessException() {
          assertThrows(EmptyResultDataAccessException.class, () -> userService.deleteById(ID));
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
}
