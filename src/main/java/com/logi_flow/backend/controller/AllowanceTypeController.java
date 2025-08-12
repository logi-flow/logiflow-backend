package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.allowanceType.request.CreateAllowanceTypeRequestDto;
import com.logi_flow.backend.dto.allowanceType.request.UpdateAllowanceTypeRequestDto;
import com.logi_flow.backend.dto.allowanceType.response.CreateAllowanceTypeResponseDto;
import com.logi_flow.backend.dto.allowanceType.response.GetAllAllowanceTypeResponseDto;
import com.logi_flow.backend.dto.allowanceType.response.UpdateAllowanceTypeResponseDto;
import com.logi_flow.backend.service.AllowanceTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.ALLOWANCE_API)
public class AllowanceTypeController {
    private final AllowanceTypeService allowanceTypeService;

    private static final String ALLOWANCE_TYPE_ID_API = "/{allowanceTypeId}";

    @PostMapping
    public ResponseEntity<ResponseDto<CreateAllowanceTypeResponseDto>> createAllowanceType(
            @Valid @RequestBody CreateAllowanceTypeRequestDto dto
    ) {
        ResponseDto<CreateAllowanceTypeResponseDto> response = allowanceTypeService.createAllowanceType(dto);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<GetAllAllowanceTypeResponseDto>> getAllAllowanceType() {
        ResponseDto<GetAllAllowanceTypeResponseDto> response = allowanceTypeService.getAllAllowanceType();
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping(ALLOWANCE_TYPE_ID_API)
    public ResponseEntity<ResponseDto<GetAllAllowanceTypeResponseDto>> getAllowanceTypeDetail(
            @PathVariable Long allowanceTypeId
    ) {
        ResponseDto<GetAllAllowanceTypeResponseDto> response = allowanceTypeService.getAllowanceTypeDetail(allowanceTypeId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @PutMapping(ALLOWANCE_TYPE_ID_API)
    public ResponseEntity<ResponseDto<UpdateAllowanceTypeResponseDto>> updateAllowanceType(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long allowanceTypeId,
            @Valid @RequestBody UpdateAllowanceTypeRequestDto dto
    ) {
        ResponseDto<UpdateAllowanceTypeResponseDto> response = allowanceTypeService.updateAllowanceType(userPrincipal, allowanceTypeId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @DeleteMapping(ALLOWANCE_TYPE_ID_API)
    public ResponseEntity<ResponseDto<Void>> deleteAllowanceType(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long allowanceTypeId
    ) {
        ResponseDto<Void> response = allowanceTypeService.deleteAllowanceType(userPrincipal, allowanceTypeId);
        return ResponseDto.toResponseEntity(HttpStatus.NO_CONTENT, response);
    }
}
