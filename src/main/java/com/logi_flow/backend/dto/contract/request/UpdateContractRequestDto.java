package com.logi_flow.backend.dto.contract.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class UpdateContractRequestDto {
    @NotNull(message = "시작일은 필수 항목입니다.")
    private LocalDate startDate;

    @NotNull(message = "종료일은 필수 항목입니다.")
    private LocalDate endDate;

    @NotNull(message = "단가는 필수 항목입니다.")
    private int baseFee;

    @NotNull(message = "중량 제한량(kg)은 필수 항목입니다.")
    private int weightLimitKg;

    @NotNull(message = "건수 제한량은 필수 항목입니다.")
    private int parcelLimit;

    @NotNull(message = "중량 한도 초과분 kg당 초과요금(원)은 필수 항목입니다.")
    private int overWeightFeePerKg;

    @NotNull(message = "건수 한도 초과분 건당 초과요금(원)은 필수 항목입니다.")
    private int overParcelFee;

    private String specialTerms;
}
