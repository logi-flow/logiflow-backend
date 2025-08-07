package com.logi_flow.backend.entity;

import com.logi_flow.backend.common.enums.DriverPayrollStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "driver_payrolls",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_driver_payrolls_period",
                        columnNames = { "driver_id", "period_start_date", "period_end_date" }
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter @Setter
@Builder
public class DriverPayroll extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DriverPayrollStatus status;

    @Column(name = "title")
    private String title;

    @Column(name = "period_start_date", nullable = false)
    private LocalDate periodStartDate;

    @Column(name = "period_end_date", nullable = false)
    private LocalDate periodEndDate;

    @Column(name = "total_allowance", nullable = false)
    private int totalAllowance;

    @Column(name = "total_deduction", nullable = false)
    private int totalDeduction;

    @Column(name = "final_amount", nullable = false)
    private int finalAmount;

    @OneToMany(mappedBy = "driverPayroll", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DriverAllowance> driverAllowances = new ArrayList<>();

    @OneToMany(mappedBy = "driverPayroll", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DriverDeduction> driverDeductions = new ArrayList<>();
}
