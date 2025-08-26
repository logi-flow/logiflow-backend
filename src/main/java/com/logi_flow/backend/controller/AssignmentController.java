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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.ASSIGNMENT_API)
public class AssignmentController {

    private final AssignmentService assignmentService;

    private static final String ASSIGNMENT_ID_API = "/{assignmentId}";
    private static final String ASSIGNMENT_STATUS_API = "/{assignmentId}/status";

    @PostMapping
    public ResponseEntity<ResponseDto<CreateAssignmentResponseDto>> createAssignment(
            @Valid @RequestBody CreateAssignmentRequestDto dto
    ) {
       ResponseDto<CreateAssignmentResponseDto> response = assignmentService.createAssignment(dto);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    @PutMapping(ASSIGNMENT_ID_API)
    public ResponseEntity<ResponseDto<UpdateAssignmentResponseDto>> updateAssignment(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long assignmentId,
            @Valid @RequestBody UpdateAssignmentRequestDto dto
    ) {
        ResponseDto<UpdateAssignmentResponseDto> response = assignmentService.updateAssignment(userPrincipal, assignmentId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @PutMapping(ASSIGNMENT_STATUS_API)
    public ResponseEntity<ResponseDto<UpdateAssignmentResponseDto>> updateAssignmentStatus(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long assignmentId,
            @Valid @RequestBody UpdateAssignmentStatusRequestDto dto
    ) {
        ResponseDto<UpdateAssignmentResponseDto> response = assignmentService.updateAssignmentStatus(userPrincipal, assignmentId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<PageDto<GetAllAssignmentResponseDto>>> getAllAssignment(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        Page<GetAllAssignmentResponseDto> result = assignmentService.getAllAssignment(page, size, sort);
        PageDto<GetAllAssignmentResponseDto> response = PageMapper.toPageDto(result, sort);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping(ASSIGNMENT_ID_API)
    public ResponseEntity<ResponseDto<GetAssignmentDetailResponseDto>> getAssignmentDetail(
            @PathVariable Long assignmentId
    ) {
        ResponseDto<GetAssignmentDetailResponseDto> response = assignmentService.getAssignmentDetail(assignmentId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @DeleteMapping(ASSIGNMENT_ID_API)
    public ResponseEntity<ResponseDto<Void>> deleteAssignment(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long assignmentId
    ) {
       ResponseDto<Void> response = assignmentService.deleteAssignment(userPrincipal, assignmentId);
       return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}