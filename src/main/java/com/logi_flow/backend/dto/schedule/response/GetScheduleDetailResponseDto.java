package com.logi_flow.backend.dto.schedule.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Builder
public class GetScheduleDetailResponseDto {
    private Long id;
    private Long allocationId;

    private String status;

    private String deliveryType;
    private Long deliveryId;
    private String collectionSitePhoneNumber;
    private String collectionSiteZipcode;
    private String collectionSiteAddress;
    private String collectionSiteAddressDetail;

    private String recipientName;
    private String recipientPhoneNumber;
    private String recipientAddress;
    private String recipientAddressDetail;
    private String recipientZipcode;

    private LocalDate allocationDate; // 배송 요청일
    private String departureTime;
    private String arrivalTime;

    private Long driverId;
    private String driverName;
    private String driverPhone;

    private String createdAt;
    private String updatedAt;
}
