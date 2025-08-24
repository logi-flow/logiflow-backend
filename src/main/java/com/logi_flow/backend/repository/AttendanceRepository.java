package com.logi_flow.backend.repository;

import com.logi_flow.backend.entity.Attendance;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Page<Attendance> findByDriverId(Long driverId, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Attendance a WHERE a.driver.id = :driverId AND a.workEnd IS NULL")
    Optional<Attendance> findOpenAttendanceForUpdate(@Param("driverId") Long driverId);

    @Override
    @EntityGraph(attributePaths = "driver")
    Page<Attendance> findAll(Pageable pageable);

    @Query("SELECT a FROM Attendance a WHERE a.driver.id = :driverId AND a.workEnd IS NULL")
    Optional<Attendance> findOpenAttendance(@Param("driverId") Long driverId);

    @Query("""
        SELECT COUNT(DISTINCT (FUNCTION('DATE', a.workStart)))
        FROM Attendance a
        WHERE a.driver.id = :driverId
            AND a.workEnd IS NOT NULL
            AND FUNCTION('DATE', a.workStart) BETWEEN :startDate AND :endDate
    """)
    int countWorkDays(@Param("driverId") Long driverId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
