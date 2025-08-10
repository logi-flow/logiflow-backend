package com.logi_flow.backend.entity;

import com.logi_flow.backend.common.enums.employee.Department;
import com.logi_flow.backend.common.enums.employee.Position;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "employees")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter @Setter
@Builder
public class Employee extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "identity_number", nullable = false, unique = true)
    private String identityNumber;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "zipcode", nullable = false)
    private String zipcode;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "address_detail")
    private String addressDetail;

    @Enumerated(EnumType.STRING)
    @Column(name = "department", nullable = false)
    private Department department;

    @Enumerated(EnumType.STRING)
    @Column(name = "position", nullable = false)
    private Position position;

    @Column(name = "company_join", nullable = false)
    private LocalDate companyJoin;
}
