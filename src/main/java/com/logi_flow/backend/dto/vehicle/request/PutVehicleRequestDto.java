package com.logi_flow.backend.dto.vehicle.request;

import com.logi_flow.backend.common.enums.driver.Fuel;
import com.logi_flow.backend.common.enums.driver.VehicleStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class PutVehicleRequestDto {
    private String vehicleNumber;
    private int capacity;
    private Fuel fuel;
    private BigDecimal mileage;
    private VehicleStatus status;
    private String modelName;
    private LocalDate modelYear;
}
