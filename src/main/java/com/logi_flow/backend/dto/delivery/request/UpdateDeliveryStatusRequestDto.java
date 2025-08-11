package com.logi_flow.backend.dto.delivery.request;

import com.logi_flow.backend.common.enums.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateDeliveryStatusRequestDto {
    private DeliveryStatus status;
    private String changeReason;
}
