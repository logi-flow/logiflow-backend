package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.common.mapper.PageMapper;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.PageDto;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.contract.request.CreateContractRequestDto;
import com.logi_flow.backend.dto.contract.request.UpdateContractRequestDto;
import com.logi_flow.backend.dto.contract.request.UpdateContractStatusRequestDto;
import com.logi_flow.backend.dto.contract.response.*;
import com.logi_flow.backend.service.ContractService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "계약 관리", description = "계약(Contract) 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.CONTRACT_API)
public class ContractController {

    private final ContractService contractService;

    private static final String CREATE_CONTRACT_API = "/{customerId}";
    private static final String CONTRACT_API = "/{contractId}";
    private static final String CONTRACT_STATUS_API = "/{contractId}/status";
    private static final String MY_CONTRACT_API = "/me";

    @Operation(summary = "신규 계약 생성", description = "계약 정보를 입력하여 고객사에 계약서를 보냄")
    @PostMapping(CREATE_CONTRACT_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'CONTRACTS_MANAGER')")
    public ResponseEntity<ResponseDto<CreateContractResponseDto>> createContract(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long customerId,
            @Valid @RequestBody CreateContractRequestDto dto
    ){
        ResponseDto<CreateContractResponseDto> response = contractService.createContract(userPrincipal, customerId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    @Operation(summary = "계약 수정", description = "새로운 계약 정보를 입력하여 기존 계약 정보를 수정")
    @PutMapping(CONTRACT_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'CONTRACTS_MANAGER')")
    public ResponseEntity<ResponseDto<UpdateContractResponseDto>> updateContract(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long contractId,
            @Valid @RequestBody UpdateContractRequestDto dto
    ){
        ResponseDto<UpdateContractResponseDto> response = contractService.updateContract(userPrincipal, contractId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "계약 상태 수정", description = "계약 상태를 입력하여 계약 상태를 수정")
    @PutMapping(CONTRACT_STATUS_API)
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<ResponseDto<UpdateContractStatusResponseDto>> updateContractStatus(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long contractId,
            @Valid @RequestBody UpdateContractStatusRequestDto dto
    ){
        ResponseDto<UpdateContractStatusResponseDto> response = contractService.updateContractStatus(userPrincipal, contractId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "계약 전체 조회", description = "전체 계약 목록 조회")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CONTRACTS_MANAGER')")
    public ResponseEntity<ResponseDto<PageDto<GetAllContractResponseDto>>> getAllContract(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetAllContractResponseDto> result = contractService.getAllContract(userPrincipal, page, size, sort);
        PageDto<GetAllContractResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "계약 상세 조회", description = "특정 계약 상세 조회")
    @GetMapping(CONTRACT_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'CONTRACTS_MANAGER', 'CUSTOMER')")
    public ResponseEntity<ResponseDto<GetContractDetailResponseDto>> getContractDetail(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long contractId
    ) {
        ResponseDto<GetContractDetailResponseDto> response = contractService.getContractDetail(userPrincipal, contractId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "고객사별 계약 전체 조회", description = "고객사별 계약 전체 목록 조회")
    @GetMapping(MY_CONTRACT_API)
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<ResponseDto<PageDto<GetAllContractResponseDto>>> getMyContracts(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {
        Page<GetAllContractResponseDto> result = contractService.getMyContracts(userPrincipal, page, size, sort);
        PageDto<GetAllContractResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "계약 삭제", description = "특정 계약의 상태를 삭제로 수정")
    @DeleteMapping(CONTRACT_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'CONTRACTS_MANAGER')")
    public ResponseEntity<ResponseDto<Void>> deleteContract(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long contractId
    ) {
        ResponseDto<Void> response = contractService.deleteContract(userPrincipal, contractId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

