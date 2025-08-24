package com.logi_flow.backend.dto.driverPayroll.response;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
public class AllowanceItemDto {
    private Long id;
    private Long allowanceTypeId;
    private String allowanceTypeCode;
    private String allowanceTypeName;
    private BigDecimal quantity;
    private Integer unitPrice;
    private Integer amount;
    private String memo;

    private String createdAt;
    private String updatedAt;
}
