package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.allowanceType.request.CreateAllowanceTypeRequestDto;
import com.logi_flow.backend.dto.allowanceType.request.UpdateAllowanceTypeRequestDto;
import com.logi_flow.backend.dto.allowanceType.response.CreateAllowanceTypeResponseDto;
import com.logi_flow.backend.dto.allowanceType.response.GetAllAllowanceTypeResponseDto;
import com.logi_flow.backend.dto.allowanceType.response.UpdateAllowanceTypeResponseDto;
import com.logi_flow.backend.service.AllowanceTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AllowanceTypeServiceImpl implements AllowanceTypeService {
    @Override
    public ResponseDto<CreateAllowanceTypeResponseDto> createAllowanceType(CreateAllowanceTypeRequestDto dto) {
        return null;
    }

    @Override
    public Page<GetAllAllowanceTypeResponseDto> getAllAllowanceType(int page, int size, String sort) {
        return null;
    }

    @Override
    public ResponseDto<GetAllAllowanceTypeResponseDto> getAllowanceTypeDetail(Long allowanceTypeId) {
        return null;
    }

    @Override
    public ResponseDto<UpdateAllowanceTypeResponseDto> updateAllowanceType(UserPrincipal userPrincipal, Long allowanceTypeId, UpdateAllowanceTypeRequestDto dto) {
        return null;
    }

    @Override
    public ResponseDto<Void> deleteAllowanceType(UserPrincipal userPrincipal, Long allowanceTypeId) {
        return null;
    }
}
