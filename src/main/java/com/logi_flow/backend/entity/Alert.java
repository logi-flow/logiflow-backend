package com.logi_flow.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "alerts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Alert extends BaseTimeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "message", nullable = false)
    private String message;
}
