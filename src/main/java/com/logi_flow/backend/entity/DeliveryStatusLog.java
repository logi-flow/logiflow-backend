package com.logi_flow.backend.entity;

import com.logi_flow.backend.common.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "deliveries_status_logs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DeliveryStatusLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changed_by", nullable = false)
    private User user;

    @Column(name = "changed_by_username", nullable = false)
    private String changedByUsername;

    @Column(name = "change_reason", nullable = false)
    private String changeReason;

    @Column(name = "prev_status", nullable = false)
    private DeliveryStatus prevStatus;

    @Column(name = "new_status", nullable = false)
    private DeliveryStatus newStatus;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

}
