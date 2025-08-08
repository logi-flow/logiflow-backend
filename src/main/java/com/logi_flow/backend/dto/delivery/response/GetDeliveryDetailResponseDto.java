package com.logi_flow.backend.dto.delivery.response;

import com.logi_flow.backend.common.enums.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class GetDeliveryDetailResponseDto {
    private Long id;
    private Long customerId;
    private LocalDateTime requestDate;
    private String item;
    private BigDecimal weight;
    private String message;
    private boolean isHidden;
    private DeliveryStatus status;

    private String pickupName;
    private String pickupPhone;
    private String pickupZipcode;
    private String pickupAddress;
    private String pickupAddressDetail;

    private String recipientName;
    private String recipientPhone;
    private String recipientZipcode;
    private String recipientAddress;
    private String recipientAddressDetail;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
