package com.logi_flow.backend.dto.customerLog.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class GetCustomerUpdateLogResponseDto {
    private Long id;
    private Long customerId;
    private String username;
    private String type;
    private String prevData;
    private String newData;
    private String createdAt;
}
