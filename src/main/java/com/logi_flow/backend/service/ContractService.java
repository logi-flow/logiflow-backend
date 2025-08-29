package com.logi_flow.backend.service;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.contract.request.CreateContractRequestDto;
import com.logi_flow.backend.dto.contract.request.UpdateContractRequestDto;
import com.logi_flow.backend.dto.contract.request.UpdateContractStatusRequestDto;
import com.logi_flow.backend.dto.contract.response.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

public interface ContractService {
    ResponseDto<CreateContractResponseDto> createContract(UserPrincipal userPrincipal, Long customerId, @Valid CreateContractRequestDto dto);

    ResponseDto<UpdateContractResponseDto> updateContract(UserPrincipal userPrincipal, Long contractId, @Valid UpdateContractRequestDto dto);

    ResponseDto<UpdateContractStatusResponseDto> updateContractStatus(UserPrincipal userPrincipal, Long contractId, @Valid UpdateContractStatusRequestDto dto);

    Page<GetAllContractResponseDto> getAllContract(UserPrincipal userPrincipal, int page, int size, String sort);

    ResponseDto<GetContractDetailResponseDto> getContractDetail(UserPrincipal userPrincipal, Long contractId);

    ResponseDto<Void> deleteContract(UserPrincipal userPrincipal, Long contractId);
}
