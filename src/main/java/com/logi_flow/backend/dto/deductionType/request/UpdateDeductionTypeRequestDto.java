package com.logi_flow.backend.dto.deductionType.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateDeductionTypeRequestDto {
    private String name;
    private String description;
    private boolean isActive;
}
