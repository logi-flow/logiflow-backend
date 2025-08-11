package com.logi_flow.backend.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "deliveries_update_logs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class DeliveryUpdateLog extends BaseTimeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changed_by")
    private User user;

    @Column(name = "changed_by_username", nullable = false)
    private String changedByUsername;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "prev_data")
    private String prevData;

    @Column(name = "new_data")
    private String newData;

}
