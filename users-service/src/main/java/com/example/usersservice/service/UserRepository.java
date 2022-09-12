package com.example.usersservice.service;

import com.example.usersservice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for work with user.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
