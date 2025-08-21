package com.logi_flow.backend.repository;

import com.logi_flow.backend.common.enums.AssignmentStatus;
import com.logi_flow.backend.entity.Assignment;
import com.logi_flow.backend.entity.Driver;
import com.logi_flow.backend.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    boolean existsByVehicleAndStatus(Vehicle vehicle, AssignmentStatus status);

    Optional<Assignment> findByVehicleAndStatus(Vehicle vehicle, AssignmentStatus status);


    @Query("SELECT a FROM Assignment a WHERE a.vehicle = :vehicle AND a.status IN ('ACTIVE', 'PAUSED')")
    Optional<Assignment> findActiveOrPausedByVehicle(@Param("vehicle") Vehicle vehicle);

    List<Assignment> findByDriverAndIsPrimaryFalseAndStatus(Driver driver, AssignmentStatus status);

    Optional<Assignment> findByDriverAndIsPrimaryTrueAndStatus(Driver driver, AssignmentStatus status);
}
