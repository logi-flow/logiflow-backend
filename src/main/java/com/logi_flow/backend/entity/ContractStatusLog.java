package com.logi_flow.backend.entity;

import com.logi_flow.backend.common.enums.ContractStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contracts_status_logs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class ContractStatusLog extends BaseTimeLog{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id")
    private Contract contract;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changed_by")
    private User changedBy;

    @Column(name = "changed_by_username", nullable = false)
    private String changedByUsername;

    @Column(name = "change_reason")
    private String changeReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "prev_status", nullable = false)
    private ContractStatus prevStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_status", nullable = false)
    private ContractStatus newStatus;
}
