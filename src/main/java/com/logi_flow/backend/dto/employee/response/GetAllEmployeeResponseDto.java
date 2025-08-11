package com.logi_flow.backend.dto.employee.response;

import com.logi_flow.backend.common.enums.employee.Department;
import com.logi_flow.backend.common.enums.employee.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Builder
public class GetAllEmployeeResponseDto {
    private Long id;
    private Long userId;
    private String name;
    private Department department;
    private Position position;
    private LocalDate companyJoin;
}
