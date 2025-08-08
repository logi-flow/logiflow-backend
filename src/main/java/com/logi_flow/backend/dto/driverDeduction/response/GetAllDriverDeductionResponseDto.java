package com.logi_flow.backend.dto.driverDeduction.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class GetAllDriverDeductionResponseDto {
    private String deductionTypeCode;
    private String deductionTypeName;
    private BigDecimal quantity;
    private int unitPrice;
    private int amount;
    private String memo;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
