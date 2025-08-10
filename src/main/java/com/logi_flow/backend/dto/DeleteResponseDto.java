package com.logi_flow.backend.dto;

import com.logi_flow.backend.common.enums.DeleteType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Builder
public class DeleteResponseDto {
    private Long recordId;
    private String tableName;
    private DeleteType deleteType;
    private LocalDate expiredAt;
    private Long deletedBy;
    private LocalDate deletedAt;
}
