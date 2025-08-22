package com.logi_flow.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
        if (quantity == null) {
            throw new IllegalArgumentException("수량(일수)은 필수 항목입니다.");
        }

        if (unitPrice < 0) {
            throw new IllegalArgumentException("단가는 0 이상이어야 합니다.");
        }

        this.amount = quantity.multiply(BigDecimal.valueOf(this.unitPrice))
                .setScale(0, RoundingMode.DOWN).intValueExact();
    }

    @PrePersist
    @PreUpdate
    private void prePersistOrUpdate() {
        calculateAmount();
    }
}
