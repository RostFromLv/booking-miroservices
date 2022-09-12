package com.example.hotelsservice.service;

import com.example.bookingcommonabstractservice.service.AbstractDataService;
import com.example.commondto.common.RoomPriceDto;
import com.example.hotelsservice.domain.RoomPrice;
import org.springframework.stereotype.Service;

@Service
public class RoomPriceServiceImpl extends AbstractDataService<Integer, RoomPrice, RoomPriceDto>
    implements RoomPriceService {

  @Override
  public RoomPriceDto create(RoomPriceDto target) {
    if (target.getStartDate()>= target.getEndDate()){
      throw new IllegalArgumentException("Start date should be less than finish date");
    }
    return super.create(target);
  }
}
