package com.logi_flow.backend.dto.vehicle.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class VehicleUpdateLogResponseDto {
    private Long id;
    private String vehicleNumber;
    private String changedByUsername;
    private String type;
    private String prevData;
    private String newData;
    private String createdAt;
}