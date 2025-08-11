package com.logi_flow.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contracts_update_logs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class ContractUpdateLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id")
    private Contract contract;

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
