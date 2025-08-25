package com.logi_flow.backend.dto.vehicle.response;

import com.logi_flow.backend.common.enums.driver.Fuel;
import com.logi_flow.backend.common.enums.driver.VehicleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Builder
public class GetVehicleDetailResponseDto {
    private Long vehicleId;
    private String vehicleNumber;
    private int capacity;
    private Fuel fuel;
    private BigDecimal mileage;
    private VehicleStatus status;
    private String modelName;
    private Integer modelYear;
    private String createdAt;
    private String updatedAt;
}
