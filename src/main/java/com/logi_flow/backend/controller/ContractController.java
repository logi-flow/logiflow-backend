package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.common.mapper.PageMapper;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.PageDto;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.attendance.response.GetMyAttendancesResponseDto;
import com.logi_flow.backend.dto.contract.request.CreateContractRequestDto;
import com.logi_flow.backend.dto.contract.request.UpdateContractRequestDto;
import com.logi_flow.backend.dto.contract.request.UpdateContractStatusRequestDto;
import com.logi_flow.backend.dto.contract.response.*;
import com.logi_flow.backend.service.ContractService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.CONTRACT_API)
public class ContractController {

    private final ContractService contractService;

    private static final String CONTRACT_API = "/{customerId}";
    private static final String CONTRACT_STATUS_API = "/{customerId}/status";

    @PostMapping(CONTRACT_API)
    public ResponseEntity<ResponseDto<CreateContractResponseDto>> createContract(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long customerId,
            @Valid @RequestBody CreateContractRequestDto dto
    ){
        ResponseDto<CreateContractResponseDto> response = contractService.createContract(userPrincipal, customerId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    @PutMapping(CONTRACT_API)
    public ResponseEntity<ResponseDto<UpdateContractResponseDto>> updateContract(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long customerId,
            @Valid @RequestBody UpdateContractRequestDto dto
    ){
        ResponseDto<UpdateContractResponseDto> response = contractService.updateContract(userPrincipal, customerId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @PutMapping(CONTRACT_STATUS_API)
    public ResponseEntity<ResponseDto<UpdateContractStatusResponseDto>> updateContractStatus(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long customerId,
            @Valid @RequestBody UpdateContractStatusRequestDto dto
    ){
        ResponseDto<UpdateContractStatusResponseDto> response = contractService.updateContractStatus(userPrincipal, customerId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<PageDto<GetAllContractResponseDto>>> getAllContract(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort

    ) {
        Page<GetAllContractResponseDto> result = contractService.getAllContract(userPrincipal, page, size, sort);
        PageDto<GetAllContractResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping(CONTRACT_API)
    public ResponseEntity<ResponseDto<GetContractDetailResponseDto>> getContractDetail(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long customerId
    ) {
        ResponseDto<GetContractDetailResponseDto> response = contractService.getContractDetail(userPrincipal, customerId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @DeleteMapping(CONTRACT_API)
    public ResponseEntity<ResponseDto<?>> deleteContract(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long customerId
    ) {
        ResponseDto<?> response = contractService.deleteContract(userPrincipal, customerId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

