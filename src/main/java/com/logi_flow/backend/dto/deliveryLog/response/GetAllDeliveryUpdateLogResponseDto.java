package com.logi_flow.backend.dto.deliveryLog.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class GetAllDeliveryUpdateLogResponseDto {
    private Long id;
    private Long deliveryId;
    private String username;
    private String type;
    private String prevData;
    private String newData;
    private String createdAt;
}
