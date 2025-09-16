package com.logi_flow.backend.dto.returnDelivery.response;

import com.logi_flow.backend.common.enums.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Builder
public class GetAllWaitingReturnDeliveryResponseDto {
    private Long id;
    private Long customerId;
    private Long customerName;
    private LocalDate requestDate;
    private String item;
    private BigDecimal weight;
    private String reason;
    private DeliveryStatus status;
    private String pickupName;
    private String recipientName;

    private String createdAt;
    private String updatedAt;
}
