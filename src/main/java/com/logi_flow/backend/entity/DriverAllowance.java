package com.logi_flow.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "driver_allowances")
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

    public void calculateAmount() {
        this.amount = quantity.multiply(new BigDecimal(this.unitPrice)).intValue();
    }
}
