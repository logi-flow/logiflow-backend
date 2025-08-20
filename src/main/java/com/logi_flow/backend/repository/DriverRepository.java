package com.logi_flow.backend.repository;

import com.logi_flow.backend.entity.Driver;
import com.logi_flow.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    Optional<Driver> findByPhoneNumber(String phoneNumber);
    Optional<Driver> findByNameAndPhoneNumber(String name, String phoneNumber);
    Optional<Driver> findByUserId(Long userId);
    Optional<Driver> findByUser(User user);
}
