package com.logi_flow.backend.dto.customer.request;

import com.logi_flow.backend.common.enums.CustomerStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateCustomerStatusRequestDto {
    private CustomerStatus status;
    private String changedReason;
}
