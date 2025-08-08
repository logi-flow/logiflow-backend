package com.logi_flow.backend.dto.driverDeduction.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class UpdateDriverDeductionRequestDto {
    @NotNull(message = "공제 항목은 필수 항목입니다.")
    private Long deductionTypeId;

    @NotNull(message = "공제 수량은 필수 항목입니다.")
    private BigDecimal quantity;

    @NotNull(message = "단가는 필수 항목입니다.")
    private int unitPrice;

    private String memo;
}
