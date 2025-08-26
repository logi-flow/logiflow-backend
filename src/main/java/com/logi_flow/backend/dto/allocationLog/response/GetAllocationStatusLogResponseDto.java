package com.logi_flow.backend.dto.allocationLog.response;

import com.logi_flow.backend.common.enums.AllocationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class GetAllocationStatusLogResponseDto {
    private Long deliveryId;
    private Long returnDeliveryId;

    private String driverName;
    private String vehicleNumber;

    private String changedByUsername;
    private String changeReason;
    private AllocationStatus prevStatus;
    private AllocationStatus newStatus;
    private String createdAt;
}
