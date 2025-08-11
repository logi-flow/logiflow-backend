package com.logi_flow.backend.dto.assignment.request;

import com.logi_flow.backend.common.enums.AssignmentStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class UpdateAssignmentRequestDto {
    @NotNull(message = "기사 ID는 필수 항목입니다.")
    private Long driverId;

    @NotEmpty(message = "차량 ID는 필수 항목입니다.")
    private List<Long> vehicleIds;

    private Boolean isPrimary;

    @NotNull(message = "상태는 필수 항목입니다.")
    private AssignmentStatus status;
}
