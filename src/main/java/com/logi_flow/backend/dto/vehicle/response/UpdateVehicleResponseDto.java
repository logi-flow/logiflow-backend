package com.logi_flow.backend.dto.vehicle.response;

import com.logi_flow.backend.common.enums.driver.VehicleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class UpdateVehicleResponseDto {
    private Long vehicleId;
    private String vehicleNumber;
    private VehicleStatus status;
    private String createdAt;
    private String updatedAt;
}
