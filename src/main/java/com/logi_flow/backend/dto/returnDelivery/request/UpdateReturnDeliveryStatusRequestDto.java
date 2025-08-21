package com.logi_flow.backend.dto.returnDelivery.request;

import com.logi_flow.backend.common.enums.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateReturnDeliveryStatusRequestDto {
    private DeliveryStatus status;
    private String changeReason;
}
