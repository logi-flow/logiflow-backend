package com.logi_flow.backend.dto.driverAllowanceLog.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class GetDriverAllowanceUpdateLogResponseDto {
    private Long id;
    private Long driverId;
    private String driverName;
    private Long payrollId;
    private String code;
    private String name;
    private String type;
    private String prevData;
    private String newData;
    private String changedByUsername;

    private String createdAt;
}
