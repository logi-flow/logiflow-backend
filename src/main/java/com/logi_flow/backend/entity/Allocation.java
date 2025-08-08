package com.logi_flow.backend.entity;

import com.logi_flow.backend.common.enums.AllocationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "allocations")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter @Setter
@Builder
public class Allocation extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignmentId;

    @Column(name = "district_name", nullable = false)
    private String districtName;

    @Column(name = "start_mileage", nullable = false)
    private BigDecimal startMileage;

    @Column(name = "end_mileage", nullable = false)
    private BigDecimal endMileage;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AllocationStatus status = AllocationStatus.ASSIGNED;

}
