package com.logi_flow.backend.entity;

import com.logi_flow.backend.common.enums.DeductionTypeStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "deduction_types")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DeductionType extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DeductionTypeStatus status;
}
