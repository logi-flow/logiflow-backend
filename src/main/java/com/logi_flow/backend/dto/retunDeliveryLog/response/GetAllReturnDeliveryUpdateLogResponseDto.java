package com.logi_flow.backend.dto.retunDeliveryLog.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class GetAllReturnDeliveryUpdateLogResponseDto {
    private Long id;
    private Long returnDeliveryId;
    private String username;
    private String type;
    private String prevData;
    private String newData;
    private String createdAt;
}
