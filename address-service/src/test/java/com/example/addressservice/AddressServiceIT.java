package com.example.addressservice;

import com.example.addressservice.model.AddressDto;
import com.example.addressservice.service.AddressServiceImpl;
import java.util.Collection;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;


@SpringBootTest
@ActiveProfiles(value = "integrationTest")
public class AddressServiceIT {

  private final AddressServiceImpl addressService;

  @Autowired
  public AddressServiceIT(AddressServiceImpl addressService) {
    this.addressService = addressService;
  }


  @BeforeEach
  void beforeEach() {
    addressService.deleteAll();
  }


  //GET ALL
  @Test
  void getAll_ShouldReturn_EmptyList() {
    Assertions.assertEquals(0, addressService.findAll().size());
  }

  @Test
  void getAll_ShouldReturn_ListOfAddressDto() {
    AddressDto addressDto = new AddressDto();
    setDtoData(addressDto);
    addressService.create(addressDto);
    addressDto.setCity("Lviv");
    addressService.create(addressDto);
    addressDto.setCity("Dublin");
    addressService.create(addressDto);

    Collection<AddressDto> actual = addressService.findAll();
    Assertions.assertNotNull(actual);
    Assertions.assertEquals(3, actual.size());
  }

  //CREATE
  @Test
  void createByCorrectDto_ShouldReturn_AddressDto() {
    AddressDto newAddress = new AddressDto();
    setDtoData(newAddress);
    AddressDto created = addressService.create(newAddress);

    Assertions.assertNotNull(created.getId());
    org.assertj.core.api.Assertions
        .assertThat(created)
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(newAddress);
  }

  @Test
  void createByNullDto_ShouldThrow_IllegalArgumentException() {
        Assertions.assertThrows(NullPointerException.class, () -> addressService.create(null));
  }


  @Test
  void createWithConstraintViolation_ShouldThrow_DataIntegrityViolationException() {
    AddressDto newDto = new AddressDto();
    setDtoData(newDto);
    addressService.create(newDto);

    AddressDto sameDto = new AddressDto();
    setDtoData(sameDto);

    DataIntegrityViolationException exception =
        Assertions.assertThrows(DataIntegrityViolationException.class,
            () -> addressService.create(sameDto));
    Assertions.assertInstanceOf(DataIntegrityViolationException.class, exception);

  }

  //UPDATE
  @Test
  void updateByCorrectDto_ShouldReturn_AddressDto() {
    AddressDto addressDto = new AddressDto();
    setDtoData(addressDto);

    AddressDto createdDto = addressService.create(addressDto);
    Assertions.assertNotNull(createdDto);

    addressDto.setCity("Lviv");
    addressDto.setId(createdDto.getId());

    AddressDto updatedDto = addressService.update(addressDto, addressDto.getId());

    Assertions.assertNotNull(updatedDto);
    Assertions.assertEquals(addressDto, updatedDto);
  }

  @Test
  void updateByNullDto_ShouldThrow_IllegalArgumentException() {
        Assertions.assertThrows(NullPointerException.class, () -> addressService.update(null,1));
  }

  @Test
  void updateByNotExistId_ShouldThrow_EntityNotFoundException() {
    AddressDto notExistDto = new AddressDto();
    setDtoData(notExistDto, 8);
    EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class,
        () -> addressService.update(notExistDto, notExistDto.getId()));
    Assertions.assertInstanceOf(EntityNotFoundException.class, exception);
  }

  @Test
  void updateWithConstraintViolation_ShouldThrow_DataIntegrityViolationException() {
    AddressDto existDto = new AddressDto();

    setDtoData(existDto);
    AddressDto createdDto = addressService.create(existDto);
    Assertions.assertNotNull(createdDto.getId());

    existDto.setCity("Lviv");
    AddressDto anotherDto = addressService.create(existDto);
    anotherDto.setId(createdDto.getId());

    DataIntegrityViolationException exception =
        Assertions.assertThrows(DataIntegrityViolationException.class,
            () -> addressService.update(anotherDto, anotherDto.getId()));

    Assertions.assertInstanceOf(DataIntegrityViolationException.class, exception);
  }

  //Get by Id
  @Test
  void getByByCorrectId_ShouldReturn_ExistedAddressDto() {
    AddressDto addressDto = new AddressDto();
    setDtoData(addressDto);
    AddressDto createdAddress = addressService.create(addressDto);

    Assertions.assertNotNull(createdAddress.getId());
    org.assertj.core.api.Assertions.assertThat(createdAddress)
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(addressDto);
  }

  @Test
  void getByNullId_ShouldReturn_NullPointerException() {

    Assertions.assertThrows(IllegalArgumentException.class, () -> addressService.findById(null));
  }

  @Test
  void getByZeroId_ShouldReturn_IllegalArgumentException() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> addressService.findById(0));
  }

  @Test
  void getByWrongId_ShouldReturn_EntityNotFoundException() {
    EntityNotFoundException exception =
        Assertions.assertThrows(EntityNotFoundException.class, () -> addressService.findById(8));
    Assertions.assertInstanceOf(EntityNotFoundException.class, exception);
  }

  //DELETE
  @Test
  void deleteByCorrectId_Should_ReturnLessListSize() {
    AddressDto addressDto = new AddressDto();
    setDtoData(addressDto);
    AddressDto createdDto = addressService.create(addressDto);
    int createdDtoId = createdDto.getId();

    Assertions.assertTrue(addressService.findById(createdDtoId).isPresent());
    addressService.deleteById(createdDtoId);

    Assertions.assertFalse(addressService.findById(createdDtoId).isEmpty());
  }

  @Test
  void deleteByNullId_ShouldReturn_NullPointerException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> addressService.deleteById(null));

  }

  @Test
  void deleteByZeroId_ShouldReturn_IllegalArgumentException() {
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> addressService.deleteById(0));
  }

  @Test
  void deleteByWrongId_ShouldReturn_EmptyResultDataAccessException() {

    Assertions.assertThrows(EmptyResultDataAccessException.class, () -> addressService.deleteById(10));

  }

  private void setDtoData(final AddressDto address, final Integer id) {
    Assert.notNull(address, "Address is null");
    address.setId(id);
    address.setStreet("Heroes street");
    address.setCountry("Ukraine");
    address.setCity("Kharkiv");
    address.setHouseNumber(15);
    address.setPostalCode("1488228");
  }


  private void setDtoData(final AddressDto addressDto) {
    setDtoData(addressDto, null);
  }

}
