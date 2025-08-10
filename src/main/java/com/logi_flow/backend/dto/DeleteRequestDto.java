package com.logi_flow.backend.dto;

import com.logi_flow.backend.common.enums.DeleteType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class DeleteRequestDto {
    private String reason;
    private DeleteType deleteType;
    private LocalDate expiredAt;
}
