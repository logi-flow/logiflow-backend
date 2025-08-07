package com.logi_flow.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "driver_deductions_update_logs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class DriverDeductionUpdateLog extends BaseTimeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_deduction_id")
    private DriverDeduction driverDeduction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changed_by")
    private User user;

    @Column(name = "changed_by_username", nullable = false)
    private String changedByUsername;

    @Column(name = "change_reason")
    private String changeReason;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "prev_data")
    private String prevData;

    @Column(name = "new_data")
    private String newData;
}
