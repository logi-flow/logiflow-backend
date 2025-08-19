package com.logi_flow.backend.repository;

import com.logi_flow.backend.common.enums.AssignmentStatus;
import com.logi_flow.backend.entity.Assignment;
import com.logi_flow.backend.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    boolean existsByVehicleAndStatus(Vehicle vehicle, AssignmentStatus status);
}
