package com.logi_flow.backend.dto.returnDelivery.response;

import com.logi_flow.backend.common.enums.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class CreateReturnDeliveryResponseDto {
    private Long id;
    private Long customerId;
    private String customerName;
    private LocalDate requestDate;
    private String item;
    private BigDecimal weight;
    private String Reason;
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
