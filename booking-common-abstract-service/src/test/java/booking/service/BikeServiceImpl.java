package booking.service;

import booking.domain.Bike;
import booking.model.BikeDto;
import com.example.bookingcommonabstractservice.service.AbstractDataService;
import org.springframework.stereotype.Service;

@Service
public class BikeServiceImpl extends AbstractDataService<Integer, Bike, BikeDto> implements BikeService {

}
