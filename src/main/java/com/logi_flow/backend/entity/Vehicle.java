package com.logi_flow.backend.entity;

import com.logi_flow.backend.common.enums.driver.Fuel;
import com.logi_flow.backend.common.enums.driver.VehicleStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "vehicles")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Vehicle extends BaseTime{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "vehicle_number", nullable = false)
    private String vehicleNumber;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    @Column(name = "fuel", nullable = false)
    private Fuel fuel;

    @Column(name = "mileage", nullable = false)
    private BigDecimal mileage;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private VehicleStatus status;

    @Column(name = "model_name", nullable = false)
    private String modelName;

    @Column(name = "model_year", nullable = false)
    private LocalDate modelYear;
}
