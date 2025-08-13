package com.logi_flow.backend.dto.driverAllowance.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateDriverAllowanceRequestDto {
    @NotNull(message = "수당 항목은 필수 항목입니다.")
    private Long allowanceTypeId;
}
