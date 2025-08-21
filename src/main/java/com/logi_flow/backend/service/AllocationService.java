package com.logi_flow.backend.service;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.allocation.request.CreateAllocationRequestDto;
import com.logi_flow.backend.dto.allocation.request.UpdateAllocationRequestDto;
import com.logi_flow.backend.dto.allocation.response.CreateAllocationResponseDto;
import com.logi_flow.backend.dto.allocation.response.UpdateAllocationResponseDto;

public interface AllocationService {
    ResponseDto<CreateAllocationResponseDto> createAllocation(CreateAllocationRequestDto dto, UserPrincipal userPrincipal);

    ResponseDto<UpdateAllocationResponseDto> updateAllocation(Long allocationId, UpdateAllocationRequestDto dto);
}
