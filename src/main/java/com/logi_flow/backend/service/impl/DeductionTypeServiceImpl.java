package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.deductionType.request.CreateDeductionTypeRequestDto;
import com.logi_flow.backend.dto.deductionType.request.UpdateDeductionTypeRequestDto;
import com.logi_flow.backend.dto.deductionType.response.CreateDeductionTypeResponseDto;
import com.logi_flow.backend.dto.deductionType.response.GetAllDeductionTypeResponseDto;
import com.logi_flow.backend.dto.deductionType.response.UpdateDeductionTypeResponseDto;
import com.logi_flow.backend.service.DeductionTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DeductionTypeServiceImpl implements DeductionTypeService {
    @Override
    public ResponseDto<CreateDeductionTypeResponseDto> createDeductionType(CreateDeductionTypeRequestDto dto) {
        return null;
    }

    @Override
    public Page<GetAllDeductionTypeResponseDto> getAllDeductionType(int page, int size, String sort) {
        return null;
    }

    @Override
    public ResponseDto<GetAllDeductionTypeResponseDto> getDeductionTypeDetail(Long deductionTypeId) {
        return null;
    }

    @Override
    public ResponseDto<UpdateDeductionTypeResponseDto> updateDeductionType(UserPrincipal userPrincipal, Long deductionTypeId, UpdateDeductionTypeRequestDto dto) {
        return null;
    }

    @Override
    public ResponseDto<Void> deleteDeductionType(UserPrincipal userPrincipal, Long deductionTypeId) {
        return null;
    }
}
