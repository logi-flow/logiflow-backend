package com.logi_flow.backend.dto.allocation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateAllocationRequestDto {
    @NotNull(message = "배송지 선택은 필수입니다.")
    private Long deliveryId;

    @NotNull(message = "배정 선택은 필수입니다.")
    private Long assignmentId;

    @NotBlank(message = "구역 선택은 필수입니다.")
    private String districtName;
}
