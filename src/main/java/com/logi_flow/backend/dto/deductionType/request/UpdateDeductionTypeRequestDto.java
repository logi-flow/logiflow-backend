package com.logi_flow.backend.dto.deductionType.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateDeductionTypeRequestDto {
    @NotBlank(message = "이름은 필수 항목입니다.")
    private String name;

    private String description;
    private boolean isActive;
}
