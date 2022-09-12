package com.example.ordersservice.service;

import com.example.ordersservice.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The repository for orders.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
}
