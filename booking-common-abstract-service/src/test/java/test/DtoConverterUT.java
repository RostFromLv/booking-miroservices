package test;

import booking.domain.Bike;
import booking.domain.Type;
import booking.model.BikeDto;
import com.example.bookingcommonabstractservice.converter.DtoConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
public class DtoConverterUT {
  private final static Integer id = 1;
  private final static Float wheelRadius = 26.4f;
  private final static Type typeKids = Type.KIDS;

  private final DtoConverter<Bike, BikeDto> converter  = new DtoConverter<>(new ModelMapper());;
  private final Bike bike = new Bike();
  private final BikeDto bikeDto = new BikeDto();

  @BeforeEach
  void beforeEach(){
    bike.setId(id);
    bike.setWheelRadius(wheelRadius);
    bike.setType(typeKids);

    bikeDto.setId(id);
    bikeDto.setWheelRadius(wheelRadius);
    bikeDto.setType(typeKids);
  }

  @Test
  void convertToDtoWithCorrectParams_ShouldReturn_convertedDto(){
    BikeDto actual = converter.convertToDto(bike,BikeDto.class);
    Assertions.assertNotNull(actual);
    Assertions.assertEquals(actual,bikeDto);
  }
  @Test
  void convertToDtoWithNullEntityParam_ShouldThrow_EntityNotFoundException(){
   Assertions.assertThrows(IllegalArgumentException.class,()->converter.convertToDto(null,BikeDto.class));
  }
  @Test
  void convertToDtoWithNullDtoParam_ShouldThrow_IllegalArgumentException(){
    Assertions.assertThrows(IllegalArgumentException.class,()->converter.convertToDto(bike,null));
  }

  @Test
  void convertToEntityWithCorrectParams_ShouldReturn_convertedEntity(){
    Bike actual = converter.convertToEntity(bikeDto,Bike.class);
    Assertions.assertNotNull(actual);
    Assertions.assertEquals(bike,actual);
  }
  @Test
  void convertToEntityWithNullDtoParam_ShouldThrow_IllegalArgumentException(){
    Assertions.assertThrows(IllegalArgumentException.class,()->converter.convertToEntity(null,Bike.class));
  }
  @Test
  void convertToEntityWithNullEntityParam_ShouldThrow_IllegalArgumentException(){
    Assertions.assertThrows(IllegalArgumentException.class,()->converter.convertToEntity(bikeDto,null));
  }
  @Test
  void updateWithCorrectParams_ShouldReturn_UpdatedEntity(){
    BikeDto dtoForUpdate = bikeDto.withType(Type.MOUNTAIN);
    Bike expected = bike.withType(Type.MOUNTAIN);
    converter.update(dtoForUpdate,bike);
    Assertions.assertNotNull(bike);
    Assertions.assertEquals(expected,bike);
  }
  @Test
  void updateWithNullDtoParam_ShouldThrow_IllegalArgumentException(){
    Assertions.assertThrows(IllegalArgumentException.class,()-> converter.update(null,bike));
  }
  @Test
  void updateWithNullEntityParam_ShouldThrow_IllegalArgumentException(){
    BikeDto dtoForUpdate = bikeDto.withType(Type.MOUNTAIN);
    Assertions.assertThrows(IllegalArgumentException.class,()-> converter.update(dtoForUpdate,null));
  }
}
