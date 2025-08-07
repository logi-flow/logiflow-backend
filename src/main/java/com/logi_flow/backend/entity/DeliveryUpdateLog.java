package com.logi_flow.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "deliveries_update_logs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DeliveryUpdateLog {
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

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "prev_data", nullable = false)
    private String prevData;

    @Column(name = "new_data", nullable = false)
    private String newData;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

}
