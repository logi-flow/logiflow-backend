package com.logi_flow.backend.dto.vehicle.response;

import com.logi_flow.backend.common.enums.driver.VehicleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class GetAllVehicleResponseDto {
    private Long vehicleId;
    private String vehicleNumber;
    private VehicleStatus status;
    private String modelName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
