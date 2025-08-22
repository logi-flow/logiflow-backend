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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.ALLOCATION_API)
public class AllocationController {
    private final AllocationService allocationService;

    @PostMapping
    public ResponseEntity<ResponseDto<CreateAllocationResponseDto>> createAllocation(@Valid @RequestBody CreateAllocationRequestDto dto, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        ResponseDto<CreateAllocationResponseDto> response = allocationService.createAllocation(dto, userPrincipal);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }


    @PutMapping("/{allocationId}")
    public ResponseEntity<ResponseDto<UpdateAllocationResponseDto>> updateAllocation(@PathVariable Long allocationId, @Valid @RequestBody UpdateAllocationRequestDto dto, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        ResponseDto<UpdateAllocationResponseDto> response = allocationService.updateAllocation(allocationId, dto, userPrincipal);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @PutMapping("/{allocationId}/status")
    public ResponseEntity<ResponseDto<UpdateAllocationResponseDto>> updateAllocationStatus(@PathVariable Long allocationId, @Valid @RequestBody UpdateAllocationStatusRequestDto dto, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        ResponseDto<UpdateAllocationResponseDto> response = allocationService.updateAllocationStatus(allocationId, dto, userPrincipal);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

}
