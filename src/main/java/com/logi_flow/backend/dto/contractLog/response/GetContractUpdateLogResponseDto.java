package com.logi_flow.backend.dto.contractLog.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class GetContractUpdateLogResponseDto {
    private Long id;
    private Long contractId;
    private String customerName;
    private String businessNumber;
    private String representativeName;
    private String changedByUsername;
    private String type;
    private String prevData;
    private String newData;
    private String createdAt;
}
