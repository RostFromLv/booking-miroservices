package com.example.addressservice.service;

import com.example.addressservice.domain.Address;
import com.example.addressservice.model.AddressDto;
import com.example.bookingcommonabstractservice.service.AbstractDataService;
import org.springframework.stereotype.Service;

/**
 * The class which implement.
 * <p></p>
 * Use this method to process {@link Address}(create,update,get,delete)
 *
 * @author Rostyslav Balushchak
 * @since 1.0.0-SNAPSHOT
 */
@Service
public class AddressServiceImpl extends AbstractDataService<Integer, Address, AddressDto> implements AddressService {
}
