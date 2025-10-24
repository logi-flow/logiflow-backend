package com.logi_flow.backend.repository;

import com.logi_flow.backend.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("""
        SELECT s
        FROM Schedule s
        JOIN s.allocation a
        JOIN a.assignment asg
        JOIN asg.driver d
        WHERE d.id = :driverId
    """)
    Page<Schedule> findByDriverId(@Param("driverId") Long driverId, Pageable pageable);

    @Query("""
        SELECT s 
        FROM Schedule s
        LEFT JOIN FETCH s.allocation a
        LEFT JOIN FETCH a.delivery d
        LEFT JOIN FETCH a.returnDelivery rd
        WHERE s.id = :id
    """)
    Optional<Schedule> findByIdWithDetails(@Param("id") Long id);
}
