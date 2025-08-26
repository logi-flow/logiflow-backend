package com.logi_flow.backend.dto.retunDeliveryLog.response;

import com.logi_flow.backend.common.enums.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class GetAllReturnDeliveryStatusLogResponseDto {
    private Long id;
    private Long returnDeliveryId;
    private String username;
    private String changeReason;
    private DeliveryStatus prevStatus;
    private DeliveryStatus newStatus;
    private String createdAt;
}
