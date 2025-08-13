package com.logi_flow.backend.service;

import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.contract.request.CreateContractRequestDto;
import com.logi_flow.backend.dto.contract.request.UpdateContractRequestDto;
import com.logi_flow.backend.dto.contract.request.UpdateContractStatusRequestDto;
import com.logi_flow.backend.dto.contract.response.*;
import jakarta.validation.Valid;

public interface ContractService {
    ResponseDto<CreateContractResponseDto> createContract(Long id, Long customerId, @Valid CreateContractRequestDto dto);

    ResponseDto<UpdateContractResponseDto> updateContract(Long id, Long customerId, @Valid UpdateContractRequestDto dto);

    ResponseDto<UpdateContractStatusResponseDto> updateContractStatus(Long id, Long customerId, @Valid UpdateContractStatusRequestDto dto);

    ResponseDto<GetAllContractResponseDto> getAllContract(Long id);

    ResponseDto<GetContractDetailResponseDto> getContractDetail(Long id, Long customerId);

    ResponseDto<?> deleteContract(Long id, Long customerId);
}
