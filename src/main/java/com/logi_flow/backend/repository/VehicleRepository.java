package com.logi_flow.backend.repository;

import com.logi_flow.backend.common.enums.driver.VehicleStatus;
import com.logi_flow.backend.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findFirstByStatus(VehicleStatus status);
}
