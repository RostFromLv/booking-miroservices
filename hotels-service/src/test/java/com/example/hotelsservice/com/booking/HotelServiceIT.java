package com.example.hotelsservice.com.booking;

import com.example.commondto.common.AddressDto;
import com.example.commondto.common.HotelDto;
import com.example.hotelsservice.service.HotelServiceImpl;
import javax.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;

@SpringBootTest
@ActiveProfiles("test")
public class HotelServiceIT {

  private final Integer idForGeneratingDto = 1000;

  private  static Integer ID = 1;
  private final static String NAME = "NAME";
  private final static Integer ADDRESS_ID = 15;

  private final HotelServiceImpl hotelService;

  @Autowired
  public HotelServiceIT(HotelServiceImpl hotelService) {
    this.hotelService = hotelService;
  }

  @BeforeEach
  void beforeEach() {
    hotelService.deleteAll();
  }

  //Create
  @Test
  void createByCorrectDto_ShouldReturn_CreatedDto() {
    HotelDto hotelDtoForCreate = generateHotelDto();
    Assert.notNull(hotelDtoForCreate, "Hotel dto cannot be null");
    HotelDto actual = hotelService.create(hotelDtoForCreate);
    Assertions.assertThat(actual)
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(hotelDtoForCreate);
  }

  @Test
  void createByNotUniqueAddress_ShouldThrow_DataIntegrityViolationException() {
    HotelDto existHotelDto = generateHotelDto();
    hotelService.create(existHotelDto);
    HotelDto hotelDtoWithSameAddress = generateHotelDto().withId(idForGeneratingDto);
    org.junit.jupiter.api.Assertions.assertThrows(
        DataIntegrityViolationException.class,
        () -> hotelService.create(hotelDtoWithSameAddress));
  }

  //Update
  @Test
  void updateByCorrectDto_ShouldReturn_UpdatedDto() {
    HotelDto hotelDto = generateHotelDto();
    HotelDto existDto = hotelService.create(hotelDto);

    HotelDto dtoForUpdate = generateHotelDto()
        .withId(existDto.getId())
        .withName("Updated_Name");

    HotelDto actual = hotelService.update(dtoForUpdate, dtoForUpdate.getId());

    Assertions.assertThat(actual).isEqualTo(dtoForUpdate);
  }

  @Test
  void updateByNotExistDtoId_ShouldThrow_EntityNotFoundException() {
    HotelDto dtoForUpdate = generateHotelDto();
    org.junit.jupiter.api.Assertions.assertThrows(EntityNotFoundException.class,
        () -> hotelService.update(dtoForUpdate, dtoForUpdate.getId()));
  }

  //Get by id
  @Test
  void getByCorrectId_ShouldReturn_ExistHotelDto() {
    HotelDto existHotelDto = hotelService.create(generateHotelDto());
    HotelDto actual = hotelService.findById(existHotelDto.getId()).get();
    org.junit.jupiter.api.Assertions.assertEquals(existHotelDto, actual);
  }

  @Test
  void getByWrongId_ShouldThrow_EntityNotFoundException() {
    org.junit.jupiter.api.Assertions.assertThrows(EntityNotFoundException.class,
        () -> hotelService.findById(1000).get());
  }

  //Get all
  @Test
  void getAll_ShouldReturn_FullList() {
    hotelService.create(generateHotelDto());
    hotelService.create(generateHotelDto().withId(idForGeneratingDto));
    org.junit.jupiter.api.Assertions.assertEquals(2, hotelService.findAll().size());
  }

  @Test
  void getAll_ShouldReturn_EmptyList() {
    org.junit.jupiter.api.Assertions.assertEquals(0, hotelService.findAll().size());
  }

  //Delete by id
  @Test
  void deleteByExistId_Should_VerifyCall() {
    HotelDto existDto = hotelService.create(generateHotelDto());
    hotelService.deleteById(existDto.getId());
    org.junit.jupiter.api.Assertions.assertFalse(
        hotelService.findById(existDto.getId()).isPresent());
  }

  @Test
  void deleteByNotExistId_ShouldReturn_EmptyResultDataAccessException() {
    org.junit.jupiter.api.Assertions.assertThrows(EmptyResultDataAccessException.class,
        () -> hotelService.deleteById(1));
  }

  HotelDto generateHotelDto() {
    AddressDto addressDto = new AddressDto();
    addressDto.setCity("City");
    addressDto.setCountry("Country");
    addressDto.setStreet("Street");
    addressDto.setPostalCode("P159");
    addressDto.setHouseNumber(15);
    addressDto.setId(ADDRESS_ID);


    HotelDto hotelDto = new HotelDto();
    hotelDto.setName(NAME);
    hotelDto.setAddressId(ADDRESS_ID);
    return hotelDto;
  }
}
