package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.contract.request.CreateContractRequestDto;
import com.logi_flow.backend.dto.contract.request.UpdateContractRequestDto;
import com.logi_flow.backend.dto.contract.request.UpdateContractStatusRequestDto;
import com.logi_flow.backend.dto.contract.response.CreateContractResponseDto;
import com.logi_flow.backend.dto.contract.response.UpdateContractResponseDto;
import com.logi_flow.backend.dto.contract.response.UpdateContractStatusResponseDto;
import com.logi_flow.backend.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ContractServiceImpl implements ContractService {
    @Override
    public ResponseDto<CreateContractResponseDto> createContract(Long id, Long customerId, CreateContractRequestDto dto) {
        return null;
    }

    @Override
    public ResponseDto<UpdateContractResponseDto> updateContract(Long id, Long customerId, UpdateContractRequestDto dto) {
        return null;
    }

    @Override
    public ResponseDto<UpdateContractStatusResponseDto> updateContractStatus(Long id, Long customerId, UpdateContractStatusRequestDto dto) {
        return null;
    }

    @Override
    public ResponseDto<?> deleteContract(Long id, Long customerId) {
        return null;
    }
}
