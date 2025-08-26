package com.logi_flow.backend.dto.employee.response;

import com.logi_flow.backend.common.enums.employee.Department;
import com.logi_flow.backend.common.enums.employee.EmployeeStatus;
import com.logi_flow.backend.common.enums.employee.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class CreateEmployeeResponseDto {
    private Long id;
    private Long userId;
    private String username;
    private String name;
    private EmployeeStatus status;
    private String identityNumberMasked;
    private String phoneNumber;
    private String email;
    private String zipcode;
    private String address;
    private String addressDetail;
    private Department department;
    private Position position;
    private LocalDate companyJoin;
    private String createdAt;
    private String updatedAt;
}
