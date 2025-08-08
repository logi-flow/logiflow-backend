package com.logi_flow.backend.dto.driverAllowance.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class GetAllDriverAllowanceResponseDto {
    private String allowanceTypeCode;
    private String allowanceTypeName;
    private BigDecimal quantity;
    private int unitPrice;
    private int amount;
    private String memo;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
