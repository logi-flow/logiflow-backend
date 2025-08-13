package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.contract.request.CreateContractRequestDto;
import com.logi_flow.backend.dto.contract.request.UpdateContractRequestDto;
import com.logi_flow.backend.dto.contract.request.UpdateContractStatusRequestDto;
import com.logi_flow.backend.dto.contract.response.*;
import com.logi_flow.backend.service.ContractService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.CUSTOMER_API)
public class ContractController {

    private final ContractService contractService;

    private static final String CONTRACT_API = "/{customerId}/contracts";
    private static final String CONTRACT_STATUS_API = "/{customerId}/contracts/status";

    @PostMapping(CONTRACT_API)
    public ResponseEntity<ResponseDto<CreateContractResponseDto>> createContract(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long customerId,
            @Valid @RequestBody CreateContractRequestDto dto
    ){
        Long id = userPrincipal.getId();
        ResponseDto<CreateContractResponseDto> response = contractService.createContract(id, customerId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    @PutMapping(CONTRACT_API)
    public ResponseEntity<ResponseDto<UpdateContractResponseDto>> updateContract(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long customerId,
            @Valid @RequestBody UpdateContractRequestDto dto
    ){
        Long id = userPrincipal.getId();
        ResponseDto<UpdateContractResponseDto> response = contractService.updateContract(id, customerId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @PutMapping(CONTRACT_STATUS_API)
    public ResponseEntity<ResponseDto<UpdateContractStatusResponseDto>> updateContractStatus(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long customerId,
            @Valid @RequestBody UpdateContractStatusRequestDto dto
    ){
        Long id = userPrincipal.getId();
        ResponseDto<UpdateContractStatusResponseDto> response = contractService.updateContractStatus(id, customerId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<GetAllContractResponseDto>> getAllContract(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        Long id = userPrincipal.getId();
        ResponseDto<GetAllContractResponseDto> response = contractService.getAllContract(id);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping(CONTRACT_API)
    public ResponseEntity<ResponseDto<GetContractDetailResponseDto>> getContractDetail(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long customerId
    ) {
        Long id = userPrincipal.getId();
        ResponseDto<GetContractDetailResponseDto> response = contractService.getContractDetail(id, customerId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @DeleteMapping(CONTRACT_API)
    public ResponseEntity<ResponseDto<?>> deleteContract(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long customerId
    ) {
        Long id = userPrincipal.getId();
        ResponseDto<?> response = contractService.deleteContract(id, customerId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

