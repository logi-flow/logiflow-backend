package com.logi_flow.backend.dto.allocationLog.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class GetAllocationUpdateLogResponseDto {
    private Long id;
    private Long allocationId;
    private Long deliveryId;
    private Long returnDeliveryId;

    private String driverName;
    private String vehicleNumber;

    private String changedByUsername;
    private String type;
    private String prevData;
    private String newData;
    private String createdAt;
}
