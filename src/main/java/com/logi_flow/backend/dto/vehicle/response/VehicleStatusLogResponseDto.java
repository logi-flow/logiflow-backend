package com.logi_flow.backend.dto.vehicle.response;

import com.logi_flow.backend.common.enums.driver.VehicleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class VehicleStatusLogResponseDto {
    private Long id;
    private Long vehicleId;
    private String changedByUsername;
    private String changeReason;
    private VehicleStatus prevStatus;
    private VehicleStatus newStatus;
    private String createdAt;
}