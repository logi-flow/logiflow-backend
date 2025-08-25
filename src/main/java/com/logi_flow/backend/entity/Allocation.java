package com.logi_flow.backend.entity;

import com.logi_flow.backend.common.enums.AllocationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "return_delivery_id")
    private ReturnDelivery returnDelivery;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    @Column(name = "district_name", nullable = false)
    private String districtName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AllocationStatus status = AllocationStatus.ASSIGNED;

}
