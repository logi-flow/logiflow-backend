package com.logi_flow.backend.dto.employeeLog.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class GetEmployeeUpdateLogResponseDto {
    private Long id;
    private Long employeeId;
    private String username;
    private String type;
    private String prevData;
    private String newData;
    private String createdAt;
}
