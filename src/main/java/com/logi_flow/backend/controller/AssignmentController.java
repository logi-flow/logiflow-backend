package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.common.mapper.PageMapper;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.PageDto;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.assignment.request.CreateAssignmentRequestDto;
import com.logi_flow.backend.dto.assignment.request.UpdateAssignmentRequestDto;
import com.logi_flow.backend.dto.assignment.request.UpdateAssignmentStatusRequestDto;
import com.logi_flow.backend.dto.assignment.response.CreateAssignmentResponseDto;
import com.logi_flow.backend.dto.assignment.response.GetAllAssignmentResponseDto;
import com.logi_flow.backend.dto.assignment.response.GetAssignmentDetailResponseDto;
import com.logi_flow.backend.dto.assignment.response.UpdateAssignmentResponseDto;
import com.logi_flow.backend.service.AssignmentService;
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

@Tag(name = "배정 관리", description = "배정(Assignment) 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.ASSIGNMENT_API)
public class AssignmentController {

    private final AssignmentService assignmentService;

    private static final String ASSIGNMENT_ID_API = "/{assignmentId}";
    private static final String ASSIGNMENT_STATUS_API = "/{assignmentId}/status";
    private static final String MY_ASSIGNMENT_INFO_API = "/me";

    @Operation(summary = "신규 배정 생성", description = "기사와 차량을 연결하면 배정 생성")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ALLOCATIONS_MANAGER')")
    public ResponseEntity<ResponseDto<CreateAssignmentResponseDto>> createAssignment(
            @Valid @RequestBody CreateAssignmentRequestDto dto
    ) {
       ResponseDto<CreateAssignmentResponseDto> response = assignmentService.createAssignment(dto);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    @Operation(summary = "배정 수정", description = "주 차량 배정 정보를 수정")
    @PutMapping(ASSIGNMENT_ID_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'ALLOCATIONS_MANAGER')")
    public ResponseEntity<ResponseDto<UpdateAssignmentResponseDto>> updateAssignment(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long assignmentId,
            @Valid @RequestBody UpdateAssignmentRequestDto dto
    ) {
        ResponseDto<UpdateAssignmentResponseDto> response = assignmentService.updateAssignment(userPrincipal, assignmentId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "배정 상태 변경", description = "배정 상태를 수동으로 수정")
    @PutMapping(ASSIGNMENT_STATUS_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'ALLOCATIONS_MANAGER')")
    public ResponseEntity<ResponseDto<UpdateAssignmentResponseDto>> updateAssignmentStatus(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long assignmentId,
            @Valid @RequestBody UpdateAssignmentStatusRequestDto dto
    ) {
        ResponseDto<UpdateAssignmentResponseDto> response = assignmentService.updateAssignmentStatus(userPrincipal, assignmentId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "내 배정 조회", description = "내가 배정된 정보 조회")
    @GetMapping(MY_ASSIGNMENT_INFO_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'ALLOCATIONS_MANAGER', 'DRIVER')")
    public ResponseEntity<ResponseDto<GetAssignmentDetailResponseDto>> getMyAssignment(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        ResponseDto<GetAssignmentDetailResponseDto> response = assignmentService.getMyAssignment(userPrincipal);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "모든 배정 조회", description = "모든 배정 정보를 리스트로 조회")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ALLOCATIONS_MANAGER')")
    public ResponseEntity<ResponseDto<PageDto<GetAllAssignmentResponseDto>>> getAllAssignment(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetAllAssignmentResponseDto> result = assignmentService.getAllAssignment(page, size, sort);
        PageDto<GetAllAssignmentResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "배정 세부 정보 조회", description = "배정 세부 정보를 조회")
    @GetMapping(ASSIGNMENT_ID_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'ALLOCATIONS_MANAGER', 'DRIVER')")
    public ResponseEntity<ResponseDto<GetAssignmentDetailResponseDto>> getAssignmentDetail(
            @PathVariable Long assignmentId
    ) {
        ResponseDto<GetAssignmentDetailResponseDto> response = assignmentService.getAssignmentDetail(assignmentId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @Operation(summary = "배정 삭제", description = "배정 상태를 삭제로 변경")
    @DeleteMapping(ASSIGNMENT_ID_API)
    @PreAuthorize("hasAnyRole('ADMIN', 'ALLOCATIONS_MANAGER')")
    public ResponseEntity<ResponseDto<Void>> deleteAssignment(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long assignmentId
    ) {
       ResponseDto<Void> response = assignmentService.deleteAssignment(userPrincipal, assignmentId);
       return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}