package com.logi_flow.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "driver_deductions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter @Setter
@Builder
public class DriverDeduction extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payroll_id", nullable = false)
    private DriverPayroll driverPayroll;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deduction_type_id", nullable = false)
    private DeductionType deductionType;

    @Column(name = "quantity", nullable = false)
    private BigDecimal quantity;

    @Column(name = "unit_price", nullable = false)
    private int unitPrice;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Column(name = "memo", columnDefinition = "TEXT")
    private String memo;

    public void calculateAmount() {
        this.amount = quantity.multiply(new BigDecimal(this.unitPrice)).intValue();
    }
}
