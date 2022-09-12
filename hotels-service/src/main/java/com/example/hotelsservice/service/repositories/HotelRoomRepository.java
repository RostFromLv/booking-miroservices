package com.example.hotelsservice.service.repositories;

import com.example.hotelsservice.domain.HotelRoom;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Use this repository to configure query request to  Hotel`s Room database.
 */
@Repository
public interface HotelRoomRepository extends JpaRepository<HotelRoom,Integer> {
  Collection<HotelRoom> findAllByHotelId(Integer hotelId);
}
