package com.logi_flow.backend.dto.driverAllowance.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class UpdateDriverAllowanceRequestDto {
    @NotNull(message = "수당 항목은 필수 항목입니다.")
    private Long allowanceTypeId;

    @NotNull(message = "수당 수량은 필수 항목입니다.")
    private BigDecimal quantity;

    @NotNull(message = "단가는 필수 항목입니다.")
    private int unitPrice;

    private String memo;
}
