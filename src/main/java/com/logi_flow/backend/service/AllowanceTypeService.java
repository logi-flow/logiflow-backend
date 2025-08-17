package com.logi_flow.backend.service;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.allowanceType.request.CreateAllowanceTypeRequestDto;
import com.logi_flow.backend.dto.allowanceType.request.UpdateAllowanceTypeRequestDto;
import com.logi_flow.backend.dto.allowanceType.response.CreateAllowanceTypeResponseDto;
import com.logi_flow.backend.dto.allowanceType.response.GetAllAllowanceTypeResponseDto;
import com.logi_flow.backend.dto.allowanceType.response.GetAllowanceTypeDetailResponseDto;
import com.logi_flow.backend.dto.allowanceType.response.UpdateAllowanceTypeResponseDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

public interface AllowanceTypeService {
    ResponseDto<CreateAllowanceTypeResponseDto> createAllowanceType(@Valid CreateAllowanceTypeRequestDto dto);
    Page<GetAllAllowanceTypeResponseDto> getAllAllowanceType(int page, int size, String sort);
    ResponseDto<GetAllowanceTypeDetailResponseDto> getAllowanceTypeDetail(Long allowanceTypeId);
    ResponseDto<UpdateAllowanceTypeResponseDto> updateAllowanceType(UserPrincipal userPrincipal, Long allowanceTypeId, @Valid UpdateAllowanceTypeRequestDto dto);
    ResponseDto<Void> deleteAllowanceType(UserPrincipal userPrincipal, Long allowanceTypeId);
}
