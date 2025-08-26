package com.logi_flow.backend.dto.customerLog.response;

import com.logi_flow.backend.common.enums.CustomerStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class GetCustomerStatusLogResponseDto {
    private Long id;
    private Long customerId;
    private String username;
    private String changeReason;
    private CustomerStatus prevStatus;
    private CustomerStatus newStatus;
    private String createdAt;
}
