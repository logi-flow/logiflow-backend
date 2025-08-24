package com.logi_flow.backend.dto.driverPayroll.response;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
public class DeductionItemDto {
    private Long id;
    private Long deductionTypeId;
    private String deductionTypeCode;
    private String deductionTypeName;
    private BigDecimal quantity;
    private Integer unitPrice;
    private Integer amount;
    private String memo;

    private String createdAt;
    private String updatedAt;
}