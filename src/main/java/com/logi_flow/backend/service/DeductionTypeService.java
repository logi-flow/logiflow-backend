package com.logi_flow.backend.service;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.deductionType.request.CreateDeductionTypeRequestDto;
import com.logi_flow.backend.dto.deductionType.request.UpdateDeductionTypeRequestDto;
import com.logi_flow.backend.dto.deductionType.response.CreateDeductionTypeResponseDto;
import com.logi_flow.backend.dto.deductionType.response.GetAllDeductionTypeResponseDto;
import com.logi_flow.backend.dto.deductionType.response.UpdateDeductionTypeResponseDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

public interface DeductionTypeService {
    ResponseDto<CreateDeductionTypeResponseDto> createDeductionType(@Valid CreateDeductionTypeRequestDto dto);
    Page<GetAllDeductionTypeResponseDto> getAllDeductionType(int page, int size, String sort);
    ResponseDto<GetAllDeductionTypeResponseDto> getDeductionTypeDetail(Long deductionTypeId);
    ResponseDto<UpdateDeductionTypeResponseDto> updateDeductionType(UserPrincipal userPrincipal, Long deductionTypeId, @Valid UpdateDeductionTypeRequestDto dto);
    ResponseDto<Void> deleteDeductionType(UserPrincipal userPrincipal, Long deductionTypeId);
}
