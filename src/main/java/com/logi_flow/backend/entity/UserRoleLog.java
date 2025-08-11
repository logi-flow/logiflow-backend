package com.logi_flow.backend.entity;

import com.logi_flow.backend.common.enums.CustomerStatus;
import com.logi_flow.backend.common.enums.user.UserRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users_role_logs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class UserRoleLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changed_by")
    private User changedBy;

    @Column(name = "changed_by_username", nullable = false)
    private String changedByUsername;

    @Column(name = "change_reason")
    private String changeReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "prev_role", nullable = false)
    private UserRole prevRole;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_role", nullable = false)
    private UserRole newRole;
}
