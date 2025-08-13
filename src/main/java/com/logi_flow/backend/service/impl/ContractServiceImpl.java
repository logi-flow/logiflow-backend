package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.contract.request.CreateContractRequestDto;
import com.logi_flow.backend.dto.contract.request.UpdateContractRequestDto;
import com.logi_flow.backend.dto.contract.request.UpdateContractStatusRequestDto;
import com.logi_flow.backend.dto.contract.response.*;
import com.logi_flow.backend.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ContractServiceImpl implements ContractService {
    @Override
    public ResponseDto<CreateContractResponseDto> createContract(UserPrincipal userPrincipal, Long customerId, CreateContractRequestDto dto) {
        return null;
    }

    @Override
    public ResponseDto<UpdateContractResponseDto> updateContract(UserPrincipal userPrincipal, Long customerId, UpdateContractRequestDto dto) {
        return null;
    }

    @Override
    public ResponseDto<UpdateContractStatusResponseDto> updateContractStatus(UserPrincipal userPrincipal, Long customerId, UpdateContractStatusRequestDto dto) {
        return null;
    }

    @Override
    public ResponseDto<GetAllContractResponseDto> getAllContract(UserPrincipal userPrincipal) {
        return null;
    }

    @Override
    public ResponseDto<GetContractDetailResponseDto> getContractDetail(UserPrincipal userPrincipal, Long customerId) {
        return null;
    }

    @Override
    public ResponseDto<?> deleteContract(UserPrincipal userPrincipal, Long customerId) {
        return null;
    }
}
