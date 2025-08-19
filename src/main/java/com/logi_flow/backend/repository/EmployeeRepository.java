package com.logi_flow.backend.repository;

import com.logi_flow.backend.entity.Driver;
import com.logi_flow.backend.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByNameAndPhone(String name, String phone);
}
