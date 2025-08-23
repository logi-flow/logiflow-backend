package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.common.mapper.PageMapper;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.PageDto;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.deductionType.request.CreateDeductionTypeRequestDto;
import com.logi_flow.backend.dto.deductionType.request.UpdateDeductionTypeRequestDto;
import com.logi_flow.backend.dto.deductionType.response.CreateDeductionTypeResponseDto;
import com.logi_flow.backend.dto.deductionType.response.GetAllDeductionTypeResponseDto;
import com.logi_flow.backend.dto.deductionType.response.GetDeductionTypeDetailResponseDto;
import com.logi_flow.backend.dto.deductionType.response.UpdateDeductionTypeResponseDto;
import com.logi_flow.backend.service.DeductionTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.DEDUCTION_API)
public class DeductionTypeController {
    private final DeductionTypeService deductionTypeService;

    private static final String DEDUCTION_TYPE_ID_API = "/{deductionTypeId}";

    @PostMapping
    public ResponseEntity<ResponseDto<CreateDeductionTypeResponseDto>> createDeductionType(
            @Valid @RequestBody CreateDeductionTypeRequestDto dto
    ) {
        ResponseDto<CreateDeductionTypeResponseDto> response = deductionTypeService.createDeductionType(dto);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<PageDto<GetAllDeductionTypeResponseDto>>> getAllDeductionType(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetAllDeductionTypeResponseDto> result = deductionTypeService.getAllDeductionType(page, size, sort);
        PageDto<GetAllDeductionTypeResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping(DEDUCTION_TYPE_ID_API)
    public ResponseEntity<ResponseDto<GetDeductionTypeDetailResponseDto>> getDeductionTypeDetail(
            @PathVariable Long deductionTypeId
    ) {
        ResponseDto<GetDeductionTypeDetailResponseDto> response = deductionTypeService.getDeductionTypeDetail(deductionTypeId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @PutMapping(DEDUCTION_TYPE_ID_API)
    public ResponseEntity<ResponseDto<UpdateDeductionTypeResponseDto>> updateDeductionType(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long deductionTypeId,
            @Valid @RequestBody UpdateDeductionTypeRequestDto dto
    ) {
        ResponseDto<UpdateDeductionTypeResponseDto> response = deductionTypeService.updateDeductionType(userPrincipal, deductionTypeId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @DeleteMapping(DEDUCTION_TYPE_ID_API)
    public ResponseEntity<ResponseDto<Void>> deleteDeductionType(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long deductionTypeId
    ) {
        ResponseDto<Void> response = deductionTypeService.deleteDeductionType(userPrincipal, deductionTypeId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }
}
