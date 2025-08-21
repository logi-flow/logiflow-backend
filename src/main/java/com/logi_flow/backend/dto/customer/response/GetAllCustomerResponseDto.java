package com.logi_flow.backend.dto.customer.response;

import com.logi_flow.backend.common.enums.CustomerStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class GetAllCustomerResponseDto {
    private Long id;
    private Long userId;
    private String name;
    private CustomerStatus status;
    private String businessNumber;
    private String representativeName;
    private String telephone;
    private String email;
    private String createdAt;
    private String updatedAt;
}
