package com.logi_flow.backend.dto.employee.response;

import com.logi_flow.backend.common.constants.Regex;
import com.logi_flow.backend.common.enums.employee.Department;
import com.logi_flow.backend.common.enums.employee.Position;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class UpdateEmployeeResponseDto {
    private Long id;
    private Long userId;
    private String name;
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
