package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.assignment.request.CreateAssignmentRequestDto;
import com.logi_flow.backend.dto.assignment.request.UpdateAssignmentRequestDto;
import com.logi_flow.backend.dto.assignment.response.CreateAssignmentResponseDto;
import com.logi_flow.backend.dto.assignment.response.GetAllAssignmentResponseDto;
import com.logi_flow.backend.dto.assignment.response.GetAssignmentDetailResponseDto;
import com.logi_flow.backend.dto.assignment.response.UpdateAssignmentResponseDto;
import com.logi_flow.backend.service.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AssignmentServiceImpl implements AssignmentService {
    @Override
    public ResponseDto<CreateAssignmentResponseDto> createAssignment(CreateAssignmentRequestDto dto) {
        return null;
    }

    @Override
    public ResponseDto<UpdateAssignmentResponseDto> updateAssignment(Long assignmentId, UpdateAssignmentRequestDto dto) {
        return null;
    }

    @Override
    public ResponseDto<GetAllAssignmentResponseDto> getAllAssignment() {
        return null;
    }

    @Override
    public ResponseDto<GetAssignmentDetailResponseDto> getAssignmentDetail(Long assignmentId) {
        return null;
    }

    @Override
    public ResponseDto<?> deleteAssignment(Long assignmentId) {
        return null;
    }
}
