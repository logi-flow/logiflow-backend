package com.logi_flow.backend.entity;

import com.logi_flow.backend.common.enums.CustomerStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customers")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter @Setter
@Builder
public class Customer extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CustomerStatus status;

    private String businessNumber;

    private String representativeName;

    private String businessType;

    private String businessItems;

    private String telephone;

    private String email;

    private String fax;

    private int businessZipCode;
}
