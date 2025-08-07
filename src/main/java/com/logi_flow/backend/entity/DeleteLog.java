package com.logi_flow.backend.entity;

import com.logi_flow.backend.common.enums.DeleteType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "delete_logs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class DeleteLog extends BaseTimeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "table_name", nullable = false)
    private String tableName;

    @Column(name = "record_id", nullable = false)
    private Long recordId;

    @Column(name = "expired_at", nullable = false)
    private LocalDate expiredAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deleted_by")
    private User user;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "delete_type", nullable = false)
    private DeleteType deleteType = DeleteType.SOFT;
}
