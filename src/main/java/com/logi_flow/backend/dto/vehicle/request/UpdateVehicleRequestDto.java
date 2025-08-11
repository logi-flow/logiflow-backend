package com.logi_flow.backend.dto.vehicle.request;

import com.logi_flow.backend.common.enums.driver.Fuel;
import com.logi_flow.backend.common.enums.driver.VehicleStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class UpdateVehicleRequestDto {
    @NotBlank(message = "자동차 번호는 필수 항목입니다.")
    private String vehicleNumber;

    @NotNull(message = "적제량은 필수 항목입니다.")
    private int capacity;

    @NotNull(message = "연료종류는 필수 항목입니다.")
    private Fuel fuel;

    @NotNull(message = "주행거리는 필수 항목입니다.")
    private BigDecimal mileage;

    @NotNull(message = "차량 종류는 필수 항목입니다.")
    private VehicleStatus status;

    @NotBlank(message = "차량 이름은 필수 항목입니다.")
    private String modelName;

    @NotNull(message = "차량 연식은 필수 항목입니다.")
    private LocalDate modelYear;
}
