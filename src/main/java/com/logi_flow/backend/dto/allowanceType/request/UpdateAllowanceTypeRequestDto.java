package com.logi_flow.backend.dto.allowanceType.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateAllowanceTypeRequestDto {
    private String name;
    private String description;
    private boolean isActive;
}
