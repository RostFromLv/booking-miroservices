package com.example.hotelsservice.service.repositories;

import com.example.hotelsservice.domain.RoomPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomPriceRepository extends JpaRepository<RoomPrice,Integer> {
}
