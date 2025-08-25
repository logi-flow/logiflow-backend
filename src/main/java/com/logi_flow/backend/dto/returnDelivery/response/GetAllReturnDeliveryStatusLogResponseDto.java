package com.logi_flow.backend.dto.returnDelivery.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class GetAllReturnDeliveryStatusLogResponseDto {
    private Long id;
    private Long returnDeliveryId;
    private String username;
    private String type;
    private String prevStatus;
    private String newStatus;
    private LocalDateTime createdAt;
}
