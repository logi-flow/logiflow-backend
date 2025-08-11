package com.logi_flow.backend.controller;

import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.allocation.request.CreateAllocationRequestDto;
import com.logi_flow.backend.dto.allocation.request.UpdateAllocationRequestDto;
import com.logi_flow.backend.dto.allocation.response.CreateAllocationResponseDto;
import com.logi_flow.backend.dto.allocation.response.UpdateAllocationResponseDto;
import com.logi_flow.backend.service.AllocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/allocations")
public class AllocationController {
    private final AllocationService allocationService;

    @PostMapping
    public ResponseEntity<ResponseDto<CreateAllocationResponseDto>> createAllocation(@Valid @RequestBody CreateAllocationRequestDto dto) {
        ResponseDto<CreateAllocationResponseDto> response = allocationService.createAllocation(dto);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }


    @PutMapping("/{allocationId}")
    public ResponseEntity<ResponseDto<UpdateAllocationResponseDto>> updateAllocation(@PathVariable Long allocationId, @Valid @RequestBody UpdateAllocationRequestDto dto) {
        ResponseDto<UpdateAllocationResponseDto> response = allocationService.updateAllocation(allocationId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

}
