package com.logi_flow.backend.dto.assignment.request;

import com.logi_flow.backend.common.enums.AssignmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateAssignmentRequestDto {
    @NotNull(message = "기사 ID는 필수 항목입니다.")
    private Long driverId;

    @NotNull(message = "차량 ID는 필수 항목입니다.")
    private Long vehicleId;

    @NotNull(message = "주 차량 설정은 필수 항목입니다.")
    private Boolean isPrimary;

    @NotNull(message = "상태는 필수 항목입니다.")
    private AssignmentStatus status;
}
