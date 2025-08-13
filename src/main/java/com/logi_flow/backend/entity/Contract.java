package com.logi_flow.backend.entity;

import com.logi_flow.backend.common.enums.ContractStatus;
import com.logi_flow.backend.common.enums.user.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "contracts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter @Setter
@Builder
public class Contract extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ContractStatus status;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "base_fee", nullable = false)
    private int baseFee;

    @Column(name = "weight_limit_kg", nullable = false)
    private int weightLimitKg;

    @Column(name = "parcel_limit", nullable = false)
    private int parcelLimit;

    @Column(name = "over_weight_fee_per_kg", nullable = false)
    private int overWeightFeePerKg;

    @Column(name = "over_parcel_fee", nullable = false)
    private int overParcelFee;

    @Column(name = "special_terms", columnDefinition = "TEXT")
    private String specialTerms;
}
