package com.logi_flow.backend.dto.driverAllowance.response;

import com.logi_flow.backend.common.enums.DriverAllowanceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Builder
public class CreateDriverAllowanceResponseDto {
    private Long id;
    private String allowanceTypeCode;
    private String allowanceTypeName;
    private BigDecimal quantity;
    private int unitPrice;
    private int amount;
    private String memo;
    private DriverAllowanceStatus status;

    private String createdAt;
    private String updatedAt;
}
