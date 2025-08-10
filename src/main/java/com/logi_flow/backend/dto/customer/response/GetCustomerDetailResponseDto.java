package com.logi_flow.backend.dto.customer.response;

import com.logi_flow.backend.common.enums.CustomerStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class GetCustomerDetailResponseDto {
    private Long id;
    private Long userId;
    private CustomerStatus status;

    private String businessNumber;
    private String representativeName;
    private String businessType;
    private String businessItems;

    private String telephone;
    private String email;
    private String fax;

    private String businessZipCode;
    private String businessAddress;
    private String businessAddressDetail;

    private String chargePosition;
    private String chargeDepartment;
    private String chargeName;
    private String chargePhone;
    private String chargeEmail;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
