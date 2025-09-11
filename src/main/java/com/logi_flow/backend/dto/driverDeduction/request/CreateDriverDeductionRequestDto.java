package com.logi_flow.backend.dto.driverDeduction.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class CreateDriverDeductionRequestDto {
    @NotNull(message = "공제 항목은 필수 항목입니다.")
    private Long deductionTypeId;

    @NotNull(message = "수량(일수)은 필수 항목입니다.")
    private BigDecimal quantity;

    @NotNull(message = "단가는 필수 항목입니다.")
    @PositiveOrZero(message = "단가는 0 이상이어야 합니다.")
    private Integer unitPrice;

    private String memo;
}
