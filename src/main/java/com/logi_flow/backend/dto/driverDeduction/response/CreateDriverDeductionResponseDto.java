package com.logi_flow.backend.dto.driverDeduction.response;

import com.logi_flow.backend.common.enums.DriverDeductionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Builder
public class CreateDriverDeductionResponseDto {
    private Long id;
    private String deductionTypeCode;
    private String deductionTypeName;
    private BigDecimal quantity;
    private int unitPrice;
    private int amount;
    private String memo;
    private DriverDeductionStatus status;

    private String createdAt;
    private String updatedAt;
}
