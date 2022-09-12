package com.example.hotelsservice.service;


import com.example.bookingcommonabstractservice.service.AbstractDataService;
import com.example.commondto.common.HotelDto;
import com.example.hotelsservice.domain.Hotel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Implementation of hotel service.
 */
@Service
public class HotelServiceImpl extends AbstractDataService<Integer, Hotel, HotelDto> implements HotelService {

  private final AddressFeignClient addressClient;

  @Autowired
  public HotelServiceImpl(AddressFeignClient addressClient) {
    this.addressClient = addressClient;
  }

  @Override
  public HotelDto create(HotelDto target) {
    Assert.isNull(target.getId());

    Integer addressId = target.getAddressId();
    Assert.notNull(addressClient.getById(addressId));
    return super.create(target);
  }
}
