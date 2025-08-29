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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "공제 내역 관리", description = "기사 급여대장 공제 내역 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.PAYROLL_API)
public class DriverDeductionController {
    private final DriverDeductionService driverDeductionService;

    private final static String DEDUCTION_API = "/{payrollId}/deductions";
    private final static String DEDUCTION_ID_API = "/{payrollId}/deductions/{deductionId}";

    @Operation(summary = "공제 내역 생성", description = "특정 급여대장에 공제 내역 등록")
    @PostMapping(DEDUCTION_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'HUMAN_RESOURCES_MANAGER')")
    public ResponseEntity<ResponseDto<CreateDriverDeductionResponseDto>> createDriverDeduction(
            @PathVariable Long payrollId,
            @Valid @RequestBody CreateDriverDeductionRequestDto dto
    ) {
        ResponseDto<CreateDriverDeductionResponseDto> response = driverDeductionService.createDriverDeduction(payrollId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    @Operation(summary = "공제 내역 상세 조회", description = "특정 급여대장에 속한 모든 공제 내역 조회")
    @GetMapping(DEDUCTION_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'HUMAN_RESOURCES_MANAGER')")
    public ResponseEntity<ResponseDto<List<GetDriverDeductionDetailResponseDto>>> getDriverDeduction(
            @PathVariable Long payrollId
    ) {
        ResponseDto<List<GetDriverDeductionDetailResponseDto>> response = driverDeductionService.getDriverDeduction(payrollId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "공제 내역 수정", description = "특정 급여대장의 공제 내역 일괄 수정")
    @PutMapping(DEDUCTION_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'HUMAN_RESOURCES_MANAGER')")
    public ResponseEntity<ResponseDto<List<UpdateDriverDeductionResponseDto>>> updateDriverDeduction(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long payrollId,
            @Valid @RequestBody UpdateDriverDeductionRequestDto dto
    ) {
        ResponseDto<List<UpdateDriverDeductionResponseDto>> response = driverDeductionService.updateDriverDeduction(userPrincipal, payrollId, dto.getItems());
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "공제 내역 삭제", description = "특정 급여대장 내의 공제 내역의 상태를 삭제로 변경")
    @DeleteMapping(DEDUCTION_ID_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'HUMAN_RESOURCES_MANAGER')")
    public ResponseEntity<ResponseDto<Void>> deleteDriverDeduction(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long payrollId,
            @PathVariable Long deductionId
    ) {
        ResponseDto<Void> response = driverDeductionService.deleteDriverDeduction(userPrincipal, payrollId, deductionId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }
}
