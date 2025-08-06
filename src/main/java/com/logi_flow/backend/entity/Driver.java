package com.logi_flow.backend.entity;

import com.logi_flow.backend.common.enums.driver.DriverDistrict;
import com.logi_flow.backend.common.enums.driver.DriverStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "drivers")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter @Setter
@Builder
public class Driver extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DriverStatus status;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "identity_number", nullable = false)
    private String identityNumber;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "zipcode", nullable = false)
    private Integer zipcode;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "address_detail", nullable = false)
    private String addressDetail;

    @Enumerated(EnumType.STRING)
    @Column(name = "district", nullable = false)
    private DriverDistrict district;

    @Column(name = "pay", nullable = false)
    private Integer pay;

    @Column(name = "company_join", nullable = false)
    private LocalDate companyJoin;
}
