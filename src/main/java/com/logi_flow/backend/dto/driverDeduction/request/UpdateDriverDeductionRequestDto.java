package com.logi_flow.backend.dto.driverDeduction.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Getter
public class UpdateDriverDeductionRequestDto {
    @NotEmpty
    private List<Item> items;

    @Getter
    public static class Item {
        @NotNull(message = "공제 내역은 필수 항목입니다.")
        private Long id;

        @NotNull(message = "수량(일수)은 필수 항목입니다.")
        private BigDecimal quantity;

        @PositiveOrZero(message = "단가는 0 이상이어야 합니다.")
        private Integer unitPrice;

        private String memo;
    }
}
