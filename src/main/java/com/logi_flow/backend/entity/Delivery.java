package com.logi_flow.backend.entity;

import com.logi_flow.backend.common.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "deliveries")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter @Setter
@Builder
public class Delivery extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "request_date", nullable = false)
    private LocalDateTime requestDate;

    @Column(name = "item", nullable = false)
    private String item;

    @Column(name = "weight", nullable = false)
    private BigDecimal weight;

    @Column(name = "message")
    private String message;

    @Column(name = "is_hidden", nullable = false)
    private boolean isHidden = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DeliveryStatus status = DeliveryStatus.REQUEST;

    @Column(name = "pickup_name", nullable = false)
    private String pickupName;

    @Column(name = "pickup_phone", nullable = false)
    private String pickupPhone;

    @Column(name = "pickup_zipcode", nullable = false)
    private int pickupZipCode;

    @Column(name = "pickup_address", nullable = false)
    private String pickupAddress;

    @Column(name = "pickup_address_detail", nullable = false)
    private String pickupAddressDetail;

    @Column(name = "recipient_name", nullable = false)
    private String recipientName;

    @Column(name = "recipient_phone", nullable = false)
    private String recipientPhone;

    @Column(name = "recipient_zipcode", nullable = false)
    private String recipientZipcode;

    @Column(name = "recipient_address", nullable = false)
    private String recipientAddress;

    @Column(name = "recipient_address_detail", nullable = false)
    private String recipientAddressDetail;

}
