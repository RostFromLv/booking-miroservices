package test;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import booking.domain.Bike;
import booking.domain.Type;
import booking.model.BikeDto;
import booking.service.BikeServiceImpl;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.internal.util.Assert;
import org.springframework.data.jpa.repository.JpaRepository;

@ExtendWith(MockitoExtension.class)
public class BikeServiceImplUT {

  private final static Integer ID = 1;
  private final static Type TYPE = Type.SPORT;
  private final static Float WHEEL_RADIUS = 27.4f;

  @Mock
  JpaRepository<Bike,Integer> repository;
  @InjectMocks
  BikeServiceImpl bikeService;


  private final Bike bike = new Bike();
  private final BikeDto bikeDto = new BikeDto();

  @BeforeEach
  void beforeEach() {
    bike.setId(ID);
    bike.setType(TYPE);
    bike.setWheelRadius(WHEEL_RADIUS);

    bikeDto.setId(ID);
    bikeDto.setType(TYPE);
    bikeDto.setWheelRadius(WHEEL_RADIUS);
  }

  //Create
  @Test
  void createByCorrectBikeDto_ShouldReturn_CreatedBikeDto() {
    when(repository.save(bike.withId(null))).thenReturn(bike);

    BikeDto actual = bikeService.create(bikeDto.withId(null));
    Assertions.assertEquals(actual, bikeDto);
  }

  @Test
  void createByNullBikeDto_ShouldThrow_NullPointerException() {
    Assertions.assertThrows(NullPointerException.class,()->bikeService.create(null));
  }

  //Update
  @Test
  void updateByCorrectBikeDto_ShouldReturn_UpdatedBikeDto() {


    when(repository.findById(ID)).thenReturn(Optional.of(bike));
    bike.setType(Type.KIDS);
    when(repository.save(bike)).thenReturn(bike);


    BikeDto actual = bikeService.update(bikeDto,ID);

    Assertions.assertEquals(actual,bikeDto);
  }
  @Test
  void updateByWrongBikeDtoId_ShouldThrow_EntityNotFoundException(){
    when(repository.findById(ID)).thenThrow(EntityNotFoundException.class);
    Assertions.assertThrows(EntityNotFoundException.class,()->bikeService.update(bikeDto,ID));
  }

  @Test
  void updateByNullBikeDto_ShouldThrow_NullPointerException() {
    Assertions.assertThrows(NullPointerException.class,()->bikeService.update(null,ID));
  }

  @Test
  void updateWithBikeNullId_ShouldThrow_IllegalArgumentException() {
    Assertions.assertThrows(IllegalArgumentException.class,()->bikeService.update(bikeDto,null));
  }

  //Get by id
  @Test
  void getByCorrectId_ShouldReturn_ExistBikeDto() {
    when(repository.findById(ID)).thenReturn(Optional.of(bike));

    Optional<BikeDto> actual = bikeService.findById(ID);
    Assert.isTrue(actual.isPresent());

    Assertions.assertEquals(actual.get(),bikeDto);
  }

  @Test
  void getByNotExistId_ShouldThrow_EntityNotExistException() {
    when(repository.findById(ID)).thenThrow(EntityNotFoundException.class);
    Assertions.assertThrows(EntityNotFoundException.class,()->bikeService.findById(ID));
  }

  @Test
  void getByNullId_ShouldThrow_IllegalArgumentException() {
    Assertions.assertThrows(IllegalArgumentException.class,()->bikeService.findById(null));
  }

  //Get all
  @Test
  void getAll_ShouldReturn_EmptyList() {
    Assertions.assertEquals(0,bikeService.findAll().size());
  }

  @Test
  void getAll_ShouldReturn_ListWithDto() {
    List<Bike> bikeList = new ArrayList<>();
    bikeList.add(bike);
    bikeList.add(bike);

    when(repository.findAll()).thenReturn(bikeList);

    Collection<BikeDto> bikeDtoCollection = bikeService.findAll();

    Assertions.assertFalse(bikeDtoCollection.isEmpty());
    Assertions.assertEquals(2,bikeDtoCollection.size());
  }

  //Delete by id
  @Test
  void deleteByCorrectId_Should_VerifyCall() {
    bikeService.deleteById(ID);
    Mockito.verify(repository,times(1)).deleteById(ID);
  }



}
