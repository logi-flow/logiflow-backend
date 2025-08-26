package com.logi_flow.backend.dto.deductionTypeLog.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class GetDeductionTypeUpdateLogResponseDto {
    private Long id;
    private String code;
    private String type;
    private String prevData;
    private String newData;
    private String changedByUsername;

    private String createdAt;
}
