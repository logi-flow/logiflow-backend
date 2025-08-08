package com.logi_flow.backend.dto.allocation.request;

import com.logi_flow.backend.common.enums.AllocationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class CreateAllocationRequestDto {
    private Long deliveryId;
    private Long assignmentId;
    private String districtName;
    private BigDecimal startMileage;
    private BigDecimal endMileage;
    private AllocationStatus status;
}
