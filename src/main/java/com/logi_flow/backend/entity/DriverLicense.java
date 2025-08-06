package com.logi_flow.backend.entity;

import com.logi_flow.backend.common.enums.driver.DriverLicenseType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "driver_licenses")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DriverLicense extends BaseTime {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    @Column(name = "driver_number", nullable = false)
    private String driverNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private DriverLicenseType type;

    @Column(name = "expired_date", nullable = false)
    private LocalDate expiredDate;
}