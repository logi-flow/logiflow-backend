package com.logi_flow.backend.service;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.contract.request.CreateContractRequestDto;
import com.logi_flow.backend.dto.contract.request.UpdateContractRequestDto;
import com.logi_flow.backend.dto.contract.request.UpdateContractStatusRequestDto;
import com.logi_flow.backend.dto.contract.response.*;
import jakarta.validation.Valid;

public interface ContractService {
    ResponseDto<CreateContractResponseDto> createContract(UserPrincipal userPrincipal, Long customerId, @Valid CreateContractRequestDto dto);

    ResponseDto<UpdateContractResponseDto> updateContract(UserPrincipal userPrincipal, Long customerId, @Valid UpdateContractRequestDto dto);

    ResponseDto<UpdateContractStatusResponseDto> updateContractStatus(UserPrincipal userPrincipal, Long customerId, @Valid UpdateContractStatusRequestDto dto);

    ResponseDto<GetAllContractResponseDto> getAllContract(UserPrincipal userPrincipal);

    ResponseDto<GetContractDetailResponseDto> getContractDetail(UserPrincipal userPrincipal, Long customerId);

    ResponseDto<?> deleteContract(UserPrincipal userPrincipal, Long customerId);
}
