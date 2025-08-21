package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.constants.ResponseCode;
import com.logi_flow.backend.common.constants.ResponseMessage;
import com.logi_flow.backend.common.enums.user.UserRole;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.allocation.request.CreateAllocationRequestDto;
import com.logi_flow.backend.dto.allocation.request.UpdateAllocationRequestDto;
import com.logi_flow.backend.dto.allocation.response.CreateAllocationResponseDto;
import com.logi_flow.backend.dto.allocation.response.UpdateAllocationResponseDto;
import com.logi_flow.backend.entity.Allocation;
import com.logi_flow.backend.entity.Assignment;
import com.logi_flow.backend.entity.Delivery;
import com.logi_flow.backend.entity.User;
import com.logi_flow.backend.repository.AllocationRepository;
import com.logi_flow.backend.repository.AssignmentRepository;
import com.logi_flow.backend.repository.DeliveryRepository;
import com.logi_flow.backend.repository.UserRepository;
import com.logi_flow.backend.service.AllocationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.naming.NoPermissionException;

@RequiredArgsConstructor
@Service
public class AllocationServiceImpl implements AllocationService {

    private final UserRepository userRepository;
    private final DeliveryRepository deliveryRepository;
    private final AssignmentRepository assignmentRepository;
    private final AllocationRepository allocationRepository;

    @Override
    public ResponseDto<CreateAllocationResponseDto> createAllocation(CreateAllocationRequestDto dto, UserPrincipal userPrincipal) {
        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(ResponseMessage.USER_NOT_FOUND));

        if(!user.getRole().getName().equals(UserRole.ALLOCATIONS_MANAGER)){
            return ResponseDto.fail("FORBIDDEN", ResponseMessage.NO_PERMISSION);
        }

        Delivery delivery = deliveryRepository.findById(dto.getDeliveryId()).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));
        Assignment assignment = assignmentRepository.findById(dto.getAssignmentId()).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        Allocation newAllocation = Allocation.builder()
                .delivery(delivery)
                .assignment(assignment)
                .districtName(dto.getDistrictName())
                .status(dto.getStatus())
                .build();

        allocationRepository.save(newAllocation);

        CreateAllocationResponseDto data = CreateAllocationResponseDto.builder()
                .id(newAllocation.getId())
                .deliveryId(newAllocation.getId())
                .assignmentId(newAllocation.getId())
                .districtName(newAllocation.getDistrictName())
                .status(newAllocation.getStatus())
                .createdAt(newAllocation.getCreatedAt())
                .updatedAt(newAllocation.getUpdatedAt())
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<UpdateAllocationResponseDto> updateAllocation(Long allocationId, UpdateAllocationRequestDto dto) {
        Delivery delivery = deliveryRepository.findById(dto.getDeliveryId()).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));
        Assignment assignment = assignmentRepository.findById(dto.getAssignmentId()).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        Allocation allocation = allocationRepository.findById(allocationId).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        if(!dto.getDeliveryId().equals(delivery.getId())) {
            allocation.setDelivery(delivery);
        }

        if(!dto.getAssignmentId().equals(assignment.getId())) {
            allocation.setAssignment(assignment);
        }

        if(!dto.getDistrictName().equals(allocation.getDistrictName())) {
            allocation.setDistrictName(allocation.getDistrictName());
        }

        if(!dto.getStatus().equals(allocation.getStatus())) {
            allocation.setStatus(allocation.getStatus());
        }

        Allocation updatedAllocation = allocationRepository.save(allocation);

        UpdateAllocationResponseDto data = UpdateAllocationResponseDto.builder()
                .id(updatedAllocation.getId())
                .deliveryId(updatedAllocation.getId())
                .assignmentId(updatedAllocation.getId())
                .districtName(updatedAllocation.getDistrictName())
                .status(updatedAllocation.getStatus())
                .createdAt(updatedAllocation.getCreatedAt())
                .updatedAt(updatedAllocation.getUpdatedAt())
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }
}
