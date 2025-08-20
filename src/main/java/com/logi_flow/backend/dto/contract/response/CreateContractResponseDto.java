package com.logi_flow.backend.dto.contract.response;

import com.logi_flow.backend.common.enums.ContractStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class CreateContractResponseDto {
    private Long id;
    private Long customerId;
    private ContractStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private int baseFee;
    private int weightLimitKg;
    private int parcelLimit;
    private int overWeightFeePerKg;
    private int overParcelFee;
    private String specialTerms;
    private String createdAt;
    private String updatedAt;
}
