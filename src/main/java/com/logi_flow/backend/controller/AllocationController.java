package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.allocation.request.CreateAllocationRequestDto;
import com.logi_flow.backend.dto.allocation.request.UpdateAllocationRequestDto;
import com.logi_flow.backend.dto.allocation.request.UpdateAllocationStatusRequestDto;
import com.logi_flow.backend.dto.allocation.response.CreateAllocationResponseDto;
import com.logi_flow.backend.dto.allocation.response.UpdateAllocationResponseDto;
import com.logi_flow.backend.service.AllocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "배차 관리", description = "배차(Allocation) 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.ALLOCATION_API)
public class AllocationController {
    private final AllocationService allocationService;

    private static final String ALLOCATION_ID_API = "/{allocationId}";
    private static final String ALLOCATION_STATUS_API = ALLOCATION_ID_API + "/status";

    @Operation(summary = "신규 배차 생성", description = "배송(반품), 배정, 배차 정보를 입력하여 배차 생성")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ALLOCATIONS_MANAGER')")
    public ResponseEntity<ResponseDto<CreateAllocationResponseDto>> createAllocation(@Valid @RequestBody CreateAllocationRequestDto dto, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        ResponseDto<CreateAllocationResponseDto> response = allocationService.createAllocation(dto, userPrincipal);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    @Operation(summary = "배차 수정", description = "새로운 배송(반품), 배정, 배차 정보를 입력하여 배차 수정")
    @PutMapping(ALLOCATION_ID_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'ALLOCATIONS_MANAGER')")
    public ResponseEntity<ResponseDto<UpdateAllocationResponseDto>> updateAllocation(@PathVariable Long allocationId, @Valid @RequestBody UpdateAllocationRequestDto dto, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        ResponseDto<UpdateAllocationResponseDto> response = allocationService.updateAllocation(allocationId, dto, userPrincipal);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "배차 상태 수정", description = "배차 상태를 입력하여 수정")
    @PutMapping(ALLOCATION_STATUS_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'ALLOCATIONS_MANAGER')")
    public ResponseEntity<ResponseDto<UpdateAllocationResponseDto>> updateAllocationStatus(@PathVariable Long allocationId, @Valid @RequestBody UpdateAllocationStatusRequestDto dto, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        ResponseDto<UpdateAllocationResponseDto> response = allocationService.updateAllocationStatus(allocationId, dto, userPrincipal);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

}
