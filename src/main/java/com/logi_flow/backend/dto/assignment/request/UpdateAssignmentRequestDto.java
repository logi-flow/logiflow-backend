package com.logi_flow.backend.dto.assignment.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateAssignmentRequestDto {
    @NotNull(message = "주 차량 설정은 필수 항목입니다.")
    private Boolean isPrimary;
}
