package com.logi_flow.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "driver_licenses_logs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class DriverLicenseLog extends BaseTimeLog{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_license_id")
    private DriverLicense driverLicense;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changed_by")
    private User user;

    @Column(name = "changed_by_username", nullable = false)
    private String changedByUsername;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "prev_data")
    private String prevData;

    @Column(name = "new_data")
    private String newData;
}
