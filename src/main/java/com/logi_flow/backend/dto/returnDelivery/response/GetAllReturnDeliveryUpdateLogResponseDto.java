package com.logi_flow.backend.dto.returnDelivery.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

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
    private LocalDateTime createdAt;
}
