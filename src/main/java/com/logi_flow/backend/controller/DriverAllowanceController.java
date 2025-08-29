package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.driverAllowance.request.CreateDriverAllowanceRequestDto;
import com.logi_flow.backend.dto.driverAllowance.request.UpdateDriverAllowanceRequestDto;
import com.logi_flow.backend.dto.driverAllowance.response.CreateDriverAllowanceResponseDto;
import com.logi_flow.backend.dto.driverAllowance.response.GetDriverAllowanceDetailResponseDto;
import com.logi_flow.backend.dto.driverAllowance.response.UpdateDriverAllowanceResponseDto;
import com.logi_flow.backend.service.DriverAllowanceService;
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

@Tag(name = "수당 내역 관리", description = "기사 급여대장 수당 내역 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.PAYROLL_API)
public class DriverAllowanceController {
    private final DriverAllowanceService driverAllowanceService;

    private final static String ALLOWANCE_API = "/{payrollId}/allowances";
    private final static String ALLOWANCE_ID_API = "/{payrollId}/allowances/{allowanceId}";

    @Operation(summary = "수당 내역 생성", description = "특정 급여대장에 수당 내역 등록")
    @PostMapping(ALLOWANCE_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'HUMAN_RESOURCES_MANAGER')")
    public ResponseEntity<ResponseDto<CreateDriverAllowanceResponseDto>> createDriverAllowance(
            @PathVariable Long payrollId,
            @Valid @RequestBody CreateDriverAllowanceRequestDto dto
    ) {
        ResponseDto<CreateDriverAllowanceResponseDto> response = driverAllowanceService.createDriverAllowance(payrollId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    @Operation(summary = "수당 내역 상세 조회", description = "특정 급여대장에 속한 모든 수당 내역 조회")
    @GetMapping(ALLOWANCE_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'HUMAN_RESOURCES_MANAGER')")
    public ResponseEntity<ResponseDto<List<GetDriverAllowanceDetailResponseDto>>> getDriverAllowance(
            @PathVariable Long payrollId
    ) {
        ResponseDto<List<GetDriverAllowanceDetailResponseDto>> response = driverAllowanceService.getDriverAllowance(payrollId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "수당 내역 수정", description = "특정 급여대장의 수당 내역 일괄 수정")
    @PutMapping(ALLOWANCE_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'HUMAN_RESOURCES_MANAGER')")
    public ResponseEntity<ResponseDto<List<UpdateDriverAllowanceResponseDto>>> updateDriverAllowance(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long payrollId,
            @Valid @RequestBody UpdateDriverAllowanceRequestDto dto
    ) {
        try {
            ResponseDto<List<UpdateDriverAllowanceResponseDto>> response = driverAllowanceService.updateDriverAllowance(userPrincipal, payrollId, dto.getItems());
            return ResponseDto.toResponseEntity(HttpStatus.OK, response);

        }  catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.toResponseEntity(HttpStatus.BAD_REQUEST,null);
        }
    }

    @Operation(summary = "수당 내역 삭제", description = "특정 급여대장 내의 수당 내역의 상태를 삭제로 변경")
    @DeleteMapping(ALLOWANCE_ID_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'HUMAN_RESOURCES_MANAGER')")
    public ResponseEntity<ResponseDto<Void>> deleteDriverAllowance(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long payrollId,
            @PathVariable Long allowanceId
    ) {
        ResponseDto<Void> response = driverAllowanceService.deleteDriverAllowance(userPrincipal, payrollId, allowanceId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }
}
