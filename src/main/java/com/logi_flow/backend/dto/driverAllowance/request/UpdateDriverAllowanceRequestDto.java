package com.logi_flow.backend.dto.driverAllowance.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Getter
public class UpdateDriverAllowanceRequestDto {
    @NotEmpty
    private List<Item> items;

    @Getter
    public static class Item {
        @NotNull(message = "수당 내역은 필수 항목입니다.")
        private Long id;

        @NotNull(message = "수량(일수)은 필수 항목입니다.")
        private BigDecimal quantity;

        @NotNull(message = "단가는 필수 항목입니다.")
        @PositiveOrZero(message = "단가는 0 이상이어야 합니다.")
        private Integer unitPrice;

        private String memo;
    }
}
