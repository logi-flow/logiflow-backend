package com.logi_flow.backend.service;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.assignment.request.CreateAssignmentRequestDto;
import com.logi_flow.backend.dto.assignment.request.UpdateAssignmentRequestDto;
import com.logi_flow.backend.dto.assignment.request.UpdateAssignmentStatusRequestDto;
import com.logi_flow.backend.dto.assignment.response.CreateAssignmentResponseDto;
import com.logi_flow.backend.dto.assignment.response.GetAllAssignmentResponseDto;
import com.logi_flow.backend.dto.assignment.response.GetAssignmentDetailResponseDto;
import com.logi_flow.backend.dto.assignment.response.UpdateAssignmentResponseDto;
import com.logi_flow.backend.entity.Vehicle;
import jakarta.validation.Valid;

import java.util.List;

public interface AssignmentService {
    ResponseDto<CreateAssignmentResponseDto> createAssignment(CreateAssignmentRequestDto dto);

    ResponseDto<UpdateAssignmentResponseDto> updateAssignment(Long assignmentId, @Valid UpdateAssignmentRequestDto dto);

    ResponseDto<UpdateAssignmentResponseDto> updateAssignmentStatus(UserPrincipal userPrincipal, Long assignmentId, @Valid UpdateAssignmentStatusRequestDto dto);

    ResponseDto<List<GetAllAssignmentResponseDto>> getAllAssignment();

    ResponseDto<GetAssignmentDetailResponseDto> getAssignmentDetail(Long assignmentId);

    ResponseDto<?> deleteAssignment(Long assignmentId);

    void pauseAssignment(Vehicle vehicle);

    void removeAssignmentByVehicle(Vehicle vehicle);

}
