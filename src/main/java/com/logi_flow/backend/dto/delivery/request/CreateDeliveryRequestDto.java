package com.logi_flow.backend.dto.delivery.request;

import com.logi_flow.backend.common.enums.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class CreateDeliveryRequestDto {
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
}
