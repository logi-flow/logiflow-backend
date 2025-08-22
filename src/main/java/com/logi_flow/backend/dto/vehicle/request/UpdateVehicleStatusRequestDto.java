package com.logi_flow.backend.dto.vehicle.request;

import com.logi_flow.backend.common.enums.driver.VehicleStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateVehicleStatusRequestDto {
    private VehicleStatus status;
    private String changeReason;
}
