package com.logi_flow.backend.entity;

import com.logi_flow.backend.common.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "return_deliveries")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ReturnDelivery extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;

    @Column(name = "request_date", nullable = false)
    private LocalDate requestDate;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DeliveryStatus status = DeliveryStatus.REQUESTED;

    @Column(name = "pickup_name", nullable = false)
    private String pickupName;

    @Column(name = "pickup_phone", nullable = false)
    private String pickupPhone;

    @Column(name = "pickup_zipcode", nullable = false)
    private String pickupZipcode;

    @Column(name = "pickup_address", nullable = false)
    private String pickupAddress;

    @Column(name = "pickup_address_detail")
    private String pickupAddressDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_site_id", nullable = false)
    private DestinationSite destinationSite;

    @Column(name = "final_fee", nullable = false)
    private int finalFee;

    @Column(name = "over_weight_fee", nullable = false)
    private int overWeightFee = 0;

    @Column(name = "over_parcel_fee", nullable = false)
    private int overParcelFee = 0;

    @Column(name = "is_over_weight", nullable = false)
    private boolean isOverWeight = false;

    @Column(name = "is_over_parcel", nullable = false)
    private boolean isOverParcel = false;
}
