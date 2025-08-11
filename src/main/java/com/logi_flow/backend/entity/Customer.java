package com.logi_flow.backend.entity;

import com.logi_flow.backend.common.enums.CustomerStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customers")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter @Setter
@Builder
public class Customer extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CustomerStatus status;

    @Column(name = "businessNumber", nullable = false, unique = true)
    private String businessNumber;

    @Column(name = "representativeName", nullable = false)
    private String representativeName;

    @Column(name = "businessType", nullable = false)
    private String businessType;

    @Column(name = "businessItems", nullable = false)
    private String businessItems;

    @Column(name = "telephone", nullable = false)
    private String telephone;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "fax")
    private String fax;

    @Column(name = "businessZipCode", nullable = false)
    private String businessZipCode;

    @Column(name = "businessAddress", nullable = false)
    private String businessAddress;

    @Column(name = "businessAddressDetail")
    private String businessAddressDetail;

    @Column(name = "chargePosition")
    private String chargePosition;

    @Column(name = "chargeDepartment")
    private String chargeDepartment;

    @Column(name = "chargeName")
    private String chargeName;

    @Column(name = "chargePhone")
    private String chargePhone;

    @Column(name = "chargeEmail")
    private String chargeEmail;

    @Column(name = "parcel_count")
    private int parcelCount;
}
