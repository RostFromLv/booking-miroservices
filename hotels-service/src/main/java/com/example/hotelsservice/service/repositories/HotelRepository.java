package com.example.hotelsservice.service.repositories;

import com.example.hotelsservice.domain.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Use this repository to configure query request to  hotel database.
 */
@Repository
public interface HotelRepository extends JpaRepository<Hotel,Integer> {

}
