package com.logi_flow.backend.service;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.deductionType.request.CreateDeductionTypeRequestDto;
import com.logi_flow.backend.dto.deductionType.request.UpdateDeductionTypeRequestDto;
import com.logi_flow.backend.dto.deductionType.response.CreateDeductionTypeResponseDto;
import com.logi_flow.backend.dto.deductionType.response.GetAllDeductionTypeResponseDto;
import com.logi_flow.backend.dto.deductionType.response.UpdateDeductionTypeResponseDto;
import jakarta.validation.Valid;

public interface DeductionTypeService {
    ResponseDto<CreateDeductionTypeResponseDto> createDeductionType(@Valid CreateDeductionTypeRequestDto dto);
    ResponseDto<GetAllDeductionTypeResponseDto> getAllDeductionType();
    ResponseDto<GetAllDeductionTypeResponseDto> getDeductionTypeDetail(Long deductionTypeId);
    ResponseDto<UpdateDeductionTypeResponseDto> updateDeductionType(UserPrincipal userPrincipal, Long deductionTypeId, @Valid UpdateDeductionTypeRequestDto dto);
    ResponseDto<Void> deleteDeductionType(UserPrincipal userPrincipal, Long deductionTypeId);
}
