package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.common.mapper.PageMapper;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.PageDto;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.driver.request.*;
import com.logi_flow.backend.dto.driver.response.*;
import com.logi_flow.backend.service.DriverService;
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
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "기사 관리", description = "기사(Driver) 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.DRIVER_API)
public class DriverController {

    private final DriverService driverService;

    private static final String MY_INFO_API = "/me";
    private static final String DRIVER_ID_API = "/{driverId}";
    private static final String UPDATE_STATUS_API = DRIVER_ID_API + "/status";
    private static final String UPDATE_PAY_API = DRIVER_ID_API + "/pay";

    @Operation(summary = "신규 기사 생성", description = "새로운 기사 정보를 입력하면 아이디 생성")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ALLOCATIONS_MANAGER')")
    public ResponseEntity<ResponseDto<CreateDriverResponseDto>> createDriver(
            @Valid @RequestPart(value = "dto") CreateDriverRequestDto dto,
            @RequestPart(value = "profileImage", required = false)MultipartFile profileImage
            ) {
        ResponseDto<CreateDriverResponseDto> response = driverService.createDriver(dto, profileImage);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    @Operation(summary = "내 정보 수정", description = "나의 정보를 수정")
    @PutMapping(MY_INFO_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'ALLOCATIONS_MANAGER', 'DRIVER')")
    public ResponseEntity<ResponseDto<UpdateDriverResponseDto>> updateDriver(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody UpdateDriverRequestDto dto
    ) {
        ResponseDto<UpdateDriverResponseDto> response = driverService.updateDriver(userPrincipal, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "기사 정보 수정", description = "관리자, 담당자에 의한 정보 수정")
    @PutMapping(DRIVER_ID_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'ALLOCATIONS_MANAGER')")
    public ResponseEntity<ResponseDto<UpdateDriverResponseDto>> updateDriverByAdmin(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long driverId,
            @Valid @RequestBody UpdateDriverByAdminRequestDto dto
    ) {
        ResponseDto<UpdateDriverResponseDto> response = driverService.updateDriverByAdmin(userPrincipal, driverId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "기사 급여 수정", description = "관리자, 담당자에 의한 급여 수정")
    @PutMapping(UPDATE_PAY_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'HUMAN_RESOURCES_MANAGE')")
    public ResponseEntity<ResponseDto<UpdateDriverPayResponseDto>> updateDriverPay(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long driverId,
            @Valid @RequestBody UpdateDriverPayRequestDto dto
    ) {
        ResponseDto<UpdateDriverPayResponseDto> response = driverService.updateDriverPay(userPrincipal, driverId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "기사 상태 수정", description = "기사의 상태를 수정")
    @PutMapping(UPDATE_STATUS_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'ALLOCATIONS_MANAGER')")
    public ResponseEntity<ResponseDto<UpdateDriverResponseDto>> updateDriverStatus(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long driverId,
            @Valid @RequestBody UpdateDriverStatusRequestDto dto
    ) {
        ResponseDto<UpdateDriverResponseDto> response = driverService.updateDriverStatus(userPrincipal, driverId, dto);
        return  ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "모든 기사 조회", description = "기사를 모두 조회")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ALLOCATIONS_MANAGER')")
    public ResponseEntity<ResponseDto<PageDto<GetAllDriverResponseDto>>> getAllDriver(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetAllDriverResponseDto> result = driverService.getAllDriver(page, size, sort);
        PageDto<GetAllDriverResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "기사 세부정보 조회", description = "기사의 세부정보를 조회")
    @GetMapping(DRIVER_ID_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'ALLOCATIONS_MANAGER', 'DRIVER')")
    public ResponseEntity<ResponseDto<GetDriverDetailResponseDto>> getDriverDetail(
            @PathVariable Long driverId
    ) {
        ResponseDto<GetDriverDetailResponseDto> response = driverService.getDriverDetail(driverId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "내 정보 조회", description = "로그인 한 기사의 정보 조회")
    @GetMapping(MY_INFO_API)
    public ResponseEntity<ResponseDto<GetDriverDetailResponseDto>> getMyInfo(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        ResponseDto<GetDriverDetailResponseDto> response = driverService.getMyInfo(userPrincipal);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "퇴사 로직", description = "퇴사한 기사를 처리하는 메서드")
    @DeleteMapping(DRIVER_ID_API)
    public ResponseEntity<ResponseDto<Void>> retiredDriver(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long driverId
    ) {
        ResponseDto<Void> response = driverService.retiredDriver(userPrincipal, driverId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}