package com.logi_flow.backend.entity;

import com.logi_flow.backend.common.enums.AssignmentStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "assignments_status_logs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class AssignmentStatusLog extends BaseTimeLog{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changed_by")
    private User user;

    @Column(name = "changed_by_username", nullable = false)
    private String changedByUsername;

    @Column(name = "change_reason")
    private String changeReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "prev_status", nullable = false)
    private AssignmentStatus prevStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_status", nullable = false)
    private AssignmentStatus newStatus;
}
