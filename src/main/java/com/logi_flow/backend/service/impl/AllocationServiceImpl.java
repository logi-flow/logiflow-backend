package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.allocation.request.CreateAllocationRequestDto;
import com.logi_flow.backend.dto.allocation.request.UpdateAllocationRequestDto;
import com.logi_flow.backend.dto.allocation.response.CreateAllocationResponseDto;
import com.logi_flow.backend.dto.allocation.response.UpdateAllocationResponseDto;
import com.logi_flow.backend.service.AllocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AllocationServiceImpl implements AllocationService {
    @Override
    public ResponseDto<CreateAllocationResponseDto> createAllocation(CreateAllocationRequestDto dto) {
        return null;
    }

    @Override
    public ResponseDto<UpdateAllocationResponseDto> updateAllocation(Long allocationId, UpdateAllocationRequestDto dto) {
        return null;
    }
}
