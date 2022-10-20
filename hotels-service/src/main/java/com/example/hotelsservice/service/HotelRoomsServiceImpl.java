package com.example.hotelsservice.service;

import com.example.bookingcommonabstractservice.converter.DtoConverter;
import com.example.bookingcommonabstractservice.service.AbstractDataService;
import com.example.commondto.common.HotelRoomDto;
import com.example.hotelsservice.domain.Hotel;
import com.example.hotelsservice.domain.HotelRoom;
import com.example.hotelsservice.domain.RoomPrice;
import com.example.hotelsservice.service.repositories.HotelRepository;
import com.example.hotelsservice.service.repositories.HotelRoomRepository;
import com.example.hotelsservice.service.repositories.RoomPriceRepository;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Hotel room class implementation.
 */
@Slf4j
@Service
public class HotelRoomsServiceImpl extends AbstractDataService<Integer, HotelRoom, HotelRoomDto>
      implements HotelRoomService {

  private final static String NULL_DTO = "Dto cannot be null";
  private final static String NULL_ID = "Id cannot be null";
  private final static String NOT_NULL_CREATE_DTO_ID = "Hotel room dto for creating should have id NULL";
  private final static String ENTITY_NOT_EXIST = "Entity with id %s  NOT exist";
  private final static String HOTEL_NOT_EXIST_BY_ID = "Hotel with id %s doesnt exist";
  private final static String rooPriceNotExist = "Room price doenst exist with id %s ";

  private final HotelRepository hotelRepository;
  private final HotelRoomRepository hotelRoomRepository;
  private final RoomPriceRepository roomPriceRepository;
  private final DtoConverter<HotelRoom, HotelRoomDto> dtoConverter = new DtoConverter<>(new ModelMapper());

  @Autowired
  public HotelRoomsServiceImpl(HotelRepository hotelRepository,
                               HotelRoomRepository hotelRoomRepository, RoomPriceRepository roomPriceRepository) {
    this.hotelRepository = hotelRepository;
    this.hotelRoomRepository = hotelRoomRepository;
    this.roomPriceRepository = roomPriceRepository;
  }

  @Transactional
  public HotelRoomDto create(HotelRoomDto createHotelRoomDto) {
    Assert.notNull(createHotelRoomDto, NULL_DTO);
    Assert.isNull(createHotelRoomDto.getId(), NOT_NULL_CREATE_DTO_ID);
    Integer hotelId = createHotelRoomDto.getHotelId();
    Integer roomPriceId = createHotelRoomDto.getRoomPriceId();

    Hotel hotel = hotelRepository
          .findById(hotelId)
          .orElseThrow(() -> new EntityNotFoundException(String.format(HOTEL_NOT_EXIST_BY_ID, hotelId)));

    RoomPrice roomPrice = roomPriceRepository.findById(roomPriceId)
          .orElseThrow(() -> new EntityNotFoundException(String.format(rooPriceNotExist, roomPriceId)));

    HotelRoom createdHotelRoom = hotelRoomRepository
          .save(dtoConverter.convertToEntity(createHotelRoomDto, HotelRoom.class).withHotel(hotel).withRoomPrice(roomPrice));

    log.trace("Created room" + createdHotelRoom);
    return dtoConverter.convertToDto(createdHotelRoom, HotelRoomDto.class);
  }

  @Transactional
  public HotelRoomDto update(HotelRoomDto updateHotelRoomDto) {
    Assert.notNull(updateHotelRoomDto, NULL_DTO);
    Assert.notNull(updateHotelRoomDto.getId());

    int hotelRoomDtoId = updateHotelRoomDto.getId();

    HotelRoom existHotelRoom = this.hotelRoomRepository.findById(hotelRoomDtoId)
          .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_EXIST, hotelRoomDtoId)));

    dtoConverter.update(updateHotelRoomDto, existHotelRoom);
    HotelRoom updatedHotelRoom = hotelRoomRepository.save(existHotelRoom);
    log.trace("Updated room" + updatedHotelRoom);
    return dtoConverter.convertToDto(updatedHotelRoom, HotelRoomDto.class);
  }

  @Transactional(readOnly = true)
  public List<HotelRoomDto> getAllRoomsByHotelId(Integer hotelId) {

    checkHotel(hotelId);

    return hotelRoomRepository
          .findAllByHotelId(hotelId)
          .stream()
          .map(hotelRoom -> dtoConverter.convertToDto(hotelRoom, HotelRoomDto.class))
          .collect(Collectors.toList());
  }

  @Transactional
  public void deleteById(Integer id) {
    Assert.notNull(id, NULL_ID);

    hotelRoomRepository.deleteById(id);
    log.trace("Deleted room with id" + id);
  }

  void checkHotel(Integer hotelId) {
    if(!hotelRepository.existsById(hotelId)){
      throw new EntityNotFoundException(String.format(HOTEL_NOT_EXIST_BY_ID, hotelId));
    }
  }

  @Override
  public List<HotelRoomDto> getByHotelId(Integer hotelId) {
    return getAllRoomsByHotelId(hotelId);
  }


}
