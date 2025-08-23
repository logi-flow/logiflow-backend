package com.logi_flow.backend.entity;

import com.logi_flow.backend.common.enums.DriverAllowanceStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(
        name = "driver_allowances",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_driver_allowances_payroll_id_allowance_type_id",
                        columnNames = { "payroll_id", "allowance_type_id" }
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter @Setter
@Builder
public class DriverAllowance extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payroll_id", nullable = false)
    private DriverPayroll driverPayroll;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "allowance_type_id", nullable = false)
    private AllowanceType allowanceType;

    @Column(name = "quantity", nullable = false)
    private BigDecimal quantity;

    @Column(name = "unit_price", nullable = false)
    private int unitPrice;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Column(name = "memo", columnDefinition = "TEXT")
    private String memo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DriverAllowanceStatus status;

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
