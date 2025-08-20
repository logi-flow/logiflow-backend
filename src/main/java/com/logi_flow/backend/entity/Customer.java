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

    @Column(name = "business_number", nullable = false, unique = true)
    private String businessNumber;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "representative_name", nullable = false)
    private String representativeName;

    @Column(name = "business_type", nullable = false)
    private String businessType;

    @Column(name = "business_items", nullable = false)
    private String businessItems;

    @Column(name = "telephone", nullable = false, unique = true)
    private String telephone;

    @Column(name = "fax")
    private String fax;

    @Column(name = "business_zipcode", nullable = false)
    private String businessZipCode;

    @Column(name = "business_address", nullable = false)
    private String businessAddress;

    @Column(name = "business_address_detail")
    private String businessAddressDetail;

    @Column(name = "charge_position")
    private String chargePosition;

    @Column(name = "charge_department")
    private String chargeDepartment;

    @Column(name = "charge_name")
    private String chargeName;

    @Column(name = "charge_phone")
    private String chargePhone;

    @Column(name = "charge_email")
    private String chargeEmail;

    @Column(name = "parcel_count")
    private int parcelCount;
}
