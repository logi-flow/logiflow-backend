package com.logi_flow.backend.dto.allocation.request;

import com.logi_flow.backend.common.enums.AllocationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateAllocationRequestDto {

    private Long deliveryId;
    private Long returnDeliveryId;

    @NotNull(message = "배정 선택은 필수입니다.")
    private Long assignmentId;

    @NotBlank(message = "구역 선택은 필수입니다.")
    private String districtName;

    @NotNull(message = "배차 상태 선택은 필수입니다.")
    private AllocationStatus status;
}
