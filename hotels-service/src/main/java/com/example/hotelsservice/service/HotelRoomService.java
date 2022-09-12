package com.example.hotelsservice.service;


import com.example.bookingcommonabstractservice.service.DataService;
import com.example.commondto.common.HotelRoomDto;
import java.util.List;

public interface HotelRoomService extends DataService<Integer, HotelRoomDto> {

  List<HotelRoomDto> getByHotelId(Integer hotelId);
}
