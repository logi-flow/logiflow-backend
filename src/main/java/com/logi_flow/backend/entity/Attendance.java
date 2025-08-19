package com.logi_flow.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "attendances",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_attendances_driver_id_work_start",
                        columnNames = { "driver_id", "work_start" }
                ),
                @UniqueConstraint(
                        name = "uq_attendances_driver_id_open_flag",
                        columnNames = { "driver_id", "open_flag" }
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter @Setter
@Builder
public class Attendance extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    @Column(name = "work_start", nullable = false)
    private LocalDateTime workStart;

    @Column(name = "work_end")
    private LocalDateTime workEnd;

    @Column(name = "open_flag", insertable = false, updatable = false)
    private Integer openFlag;
}
