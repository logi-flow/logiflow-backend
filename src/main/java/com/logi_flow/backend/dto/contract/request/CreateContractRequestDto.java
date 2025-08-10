package com.logi_flow.backend.dto.contract.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class CreateContractRequestDto {
    private LocalDate stratDate;
    private LocalDate endDate;
    private int price;
    private int volumeLimit;
    private String specialTerms;
}
