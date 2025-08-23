package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.driverDeduction.request.CreateDriverDeductionRequestDto;
import com.logi_flow.backend.dto.driverDeduction.request.UpdateDriverDeductionRequestDto;
import com.logi_flow.backend.dto.driverDeduction.response.CreateDriverDeductionResponseDto;
import com.logi_flow.backend.dto.driverDeduction.response.GetDriverDeductionDetailResponseDto;
import com.logi_flow.backend.dto.driverDeduction.response.UpdateDriverDeductionResponseDto;
import com.logi_flow.backend.service.DriverDeductionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.PAYROLL_API)
public class DriverDeductionController {
    private final DriverDeductionService driverDeductionService;

    private final static String DEDUCTION_API = "/{payrollId}/deduction";
    private final static String DEDUCTION_ID_API = "/{payrollId}/deduction/{deductionId}";

    @PostMapping(DEDUCTION_API)
    public ResponseEntity<ResponseDto<CreateDriverDeductionResponseDto>> createDriverDeduction(
            @PathVariable Long payrollId,
            @Valid @RequestBody CreateDriverDeductionRequestDto dto
    ) {
        ResponseDto<CreateDriverDeductionResponseDto> response = driverDeductionService.createDriverDeduction(payrollId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    @GetMapping(DEDUCTION_API)
    public ResponseEntity<ResponseDto<List<GetDriverDeductionDetailResponseDto>>> getDriverDeduction(
            @PathVariable Long payrollId
    ) {
        ResponseDto<List<GetDriverDeductionDetailResponseDto>> response = driverDeductionService.getDriverDeduction(payrollId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @PutMapping(DEDUCTION_API)
    public ResponseEntity<ResponseDto<List<UpdateDriverDeductionResponseDto>>> updateDriverDeduction(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long payrollId,
            @Valid @RequestBody UpdateDriverDeductionRequestDto dto
    ) {
        ResponseDto<List<UpdateDriverDeductionResponseDto>> response = driverDeductionService.updateDriverDeduction(userPrincipal, payrollId, dto.getItems());
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @DeleteMapping(DEDUCTION_ID_API)
    public ResponseEntity<ResponseDto<Void>> deleteDriverDeduction(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long payrollId,
            @PathVariable Long deductionId
    ) {
        ResponseDto<Void> response = driverDeductionService.deleteDriverDeduction(userPrincipal, payrollId, deductionId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }
}
