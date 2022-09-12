package com.example.addressservice;//package com.booking.service;
//
//import com.booking.data.converter.DtoConverter;
//import com.booking.domain.Address;
//import com.booking.model.AddressDto;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.modelmapper.ModelMapper;
//
//import javax.persistence.EntityExistsException;
//import javax.persistence.EntityNotFoundException;
//import java.util.ArrayList;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class AddressServiceUT {
//
//    private final static Integer CORRECT_ID = 1;
//    private final static Integer WRONG_ID = 5;
//    private final static Integer ZERO_ID = 0;
//    private final static Integer NULL_ID = null;
//
//    @Mock
//    AddressRepository addressRepository;
//    @Mock
//    DtoConverter<Address,AddressDto> dtoConverter = new DtoConverter<>(new ModelMapper());
//
//    @InjectMocks
//    AddressServiceImpl addressService;
//
//    private final AddressDto nullAddressDto = null;
//    private final AddressDto correctAddressDto = new AddressDto();
//    private final AddressDto wrongAddressDto = new AddressDto();
//    private final Address correctAddress = new Address();
//    private final List<Address> emptyList = new ArrayList<>();
//    private final List<Address> addressList = new ArrayList<>();
//    private final List<AddressDto> addressDtoList = new LinkedList<>();
//
//
//    @BeforeEach
//    public void setUp() {
//        correctAddress.setId(CORRECT_ID);
//        correctAddress.setCity("ADDRESS_CITY_1");
//
//        correctAddressDto.setId(CORRECT_ID);
//        correctAddressDto.setCity("AddressDto_city");
//
//        wrongAddressDto.setId(WRONG_ID);
//        wrongAddressDto.setCity("DTO_WRONG_CITY");
//
//        addressList.add(correctAddress);
//        addressList.add(correctAddress);
//
//        addressDtoList.add(correctAddressDto);
//        addressDtoList.add(correctAddressDto);
//    }
//
//    @AfterEach
//    public void clean() {
//        Mockito.reset(addressRepository, dtoConverter);
//    }
//
//    //-- getAll method tests
//    @Test
//    void getAll_MustReturnEmptyList() {
//        when(addressRepository.findAll()).thenReturn(emptyList);
//
//        List<AddressDto> actual = addressService.getAll();
//
//        Assertions.assertThat(actual).isEmpty();
//    }
//
//    @Test
//    void getAll_MustReturnListOfAddress() {
//        when(addressRepository.findAll()).thenReturn(addressList);
//        when(dtoConverter.convertToDto(correctAddress, AddressDto.class)).thenReturn(correctAddressDto);
//
//        List<AddressDto> actual = addressService.getAll();
//
//        Assertions.assertThat(actual).isEqualTo(addressDtoList);
//
//    }
//
//    //-- getById method tests
//    @Test
//    void getByCorrectId_MustReturnCorrectAddress() {
//        when(addressRepository.findById(CORRECT_ID)).thenReturn(Optional.of(correctAddress));
//        when(dtoConverter.convertToDto(correctAddress, AddressDto.class)).thenReturn(correctAddressDto);
//
//        AddressDto actual = addressService.getById(CORRECT_ID);
//
//        Assertions.assertThat(actual).isEqualTo(correctAddressDto);
//    }
//
//    @Test
//    void getByWrongId_MustReturnEntityNotFoundException() {
//
//        when(addressRepository.existsById(WRONG_ID)).thenReturn(false);
//
//        EntityNotFoundException actualException = org.junit.jupiter.api.Assertions
//                .assertThrows(EntityNotFoundException.class, () -> addressService.getById(WRONG_ID));
//        Assertions.assertThat(actualException.getMessage()).contains(WRONG_ID + "");
//    }
//
//    @Test
//    void getByNullId_ShouldThrownIllegalArgumentException(){
//        IllegalArgumentException actual = org.junit.jupiter.api.Assertions
//                .assertThrows(IllegalArgumentException.class,()-> addressService.getById(ZERO_ID));
//        Assertions.assertThat(actual).hasMessageContaining(1+"");
//    }
//    @Test
//    void getByZeroId_ShouldThrowNullPointerException(){
//        NullPointerException actual = org.junit.jupiter.api.Assertions
//                .assertThrows(NullPointerException.class,()-> addressService.getById(NULL_ID));
//
//        Assertions.assertThat(actual).isInstanceOf(NullPointerException.class);
//    }
//
//    //-- delete method tests
//    @Test
//    void deleteByCorrectIdCall_ShouldBeVerified() {
//        addressRepository.deleteById(CORRECT_ID);
//        Mockito.verify(addressRepository, times(1)).deleteById(CORRECT_ID);
//    }
//
//    @Test
//    void deleteByNullId_ShouldThrownIllegalArgumentException(){
//        IllegalArgumentException actual = org.junit.jupiter.api.Assertions
//                .assertThrows(IllegalArgumentException.class,()-> addressService.deleteById(ZERO_ID));
//        Assertions.assertThat(actual).hasMessageContaining(1+"");
//    }
//    @Test
//    void deleteByZeroId_ShouldThrowNullPointerException(){
//        NullPointerException actual = org.junit.jupiter.api.Assertions
//                .assertThrows(NullPointerException.class,()-> addressService.deleteById(NULL_ID));
//
//        Assertions.assertThat(actual).isInstanceOf(NullPointerException.class);
//    }
//
//    //-- create method tests
//    @Test
//    void createByCorrectAddressDto_MustReturnAddressDto() {
//        when(addressRepository.existsById(CORRECT_ID)).thenReturn(false);
//        when(dtoConverter.convertToEntity(correctAddressDto, Address.class)).thenReturn(correctAddress);
//        when(addressRepository.save(correctAddress)).thenReturn(correctAddress);
//        when(dtoConverter.convertToDto(correctAddress, AddressDto.class)).thenReturn(correctAddressDto);
//
//        AddressDto actual = addressService.create(correctAddressDto);
//
//        Assertions.assertThat(actual).isEqualTo(correctAddressDto);
//    }
//
//    @Test
//    void createByExistId_MustThrowEntityExistException() {
//        when(addressRepository.existsById(WRONG_ID)).thenReturn(true);
//        EntityExistsException actualException = org.junit.jupiter.api.Assertions
//                .assertThrows(EntityExistsException.class, () -> addressService.create(wrongAddressDto));
//
//        Assertions.assertThat(actualException.getMessage()).contains(WRONG_ID + "");
//    }
//
//    @Test
//    void createByNullAddressDto_MustThrowNullPointerException(){
//        IllegalArgumentException actual  = org.junit.jupiter.api.Assertions
//                .assertThrows(IllegalArgumentException.class, ()->addressService.create(nullAddressDto));
//        Assertions.assertThat(actual).hasMessageContaining("NULL");
//    }
//
//    //-- update method tests
//    @Test
//    void updateWithCorrectDto_MustReturnAddressDto() {
//        when(addressRepository.existsById(CORRECT_ID)).thenReturn(true);
//        when(dtoConverter.convertToEntity(correctAddressDto, Address.class)).thenReturn(correctAddress);
//        when(addressRepository.save(correctAddress)).thenReturn(correctAddress);
//        when(dtoConverter.convertToDto(correctAddress, AddressDto.class)).thenReturn(correctAddressDto);
//
//        AddressDto actual = addressService.update(correctAddressDto,CORRECT_ID);
//        Assertions.assertThat(actual).isEqualTo(correctAddressDto);
//    }
//
//    @Test
//    void updateByWrongId_MustThrowEntityNotFoundException() {
//        when(addressRepository.existsById(WRONG_ID)).thenReturn(false);
//        EntityNotFoundException actualException = org.junit.jupiter.api.Assertions
//                .assertThrows(EntityNotFoundException.class, () -> addressService.update(wrongAddressDto,WRONG_ID));
//
//        Assertions.assertThat(actualException.getMessage()).contains(WRONG_ID + "");
//    }
//
//    @Test
//    void updateByNullAddressDto_MustThrowIllegalArgumentException(){
//        IllegalArgumentException actual = org.junit.jupiter.api.Assertions
//        .assertThrows(IllegalArgumentException.class,()-> addressService.update(nullAddressDto,CORRECT_ID));
//        Assertions.assertThat(actual).hasMessageContaining("NULL");
//    }
//
//}
//
