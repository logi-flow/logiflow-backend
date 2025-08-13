package com.logi_flow.backend.dto.driverDeduction.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateDriverDeductionRequestDto {
    @NotNull(message = "공제 항목은 필수 항목입니다.")
    private Long deductionTypeId;
}
