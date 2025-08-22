package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.constants.ResponseCode;
import com.logi_flow.backend.common.constants.ResponseMessage;
import com.logi_flow.backend.common.enums.AllocationStatus;
import com.logi_flow.backend.common.enums.user.UserRole;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.allocation.request.CreateAllocationRequestDto;
import com.logi_flow.backend.dto.allocation.request.UpdateAllocationRequestDto;
import com.logi_flow.backend.dto.allocation.request.UpdateAllocationStatusRequestDto;
import com.logi_flow.backend.dto.allocation.response.CreateAllocationResponseDto;
import com.logi_flow.backend.dto.allocation.response.UpdateAllocationResponseDto;
import com.logi_flow.backend.entity.*;
import com.logi_flow.backend.repository.*;
import com.logi_flow.backend.service.AllocationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AllocationServiceImpl implements AllocationService {

    private final UserRepository userRepository;
    private final DeliveryRepository deliveryRepository;
    private final AssignmentRepository assignmentRepository;
    private final AllocationRepository allocationRepository;
    private final ScheduleRepository scheduleRepository;

    private final AllocationUpdateLogRepository allocationUpdateLogRepository;
    private final AllocationStatusLogRepository allocationStatusLogRepository;

    @Override
    public ResponseDto<CreateAllocationResponseDto> createAllocation(CreateAllocationRequestDto dto, UserPrincipal userPrincipal) {
        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

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


        Schedule newSchedule = Schedule.builder()
                .allocation(newAllocation)
                .allocationDate(delivery.getRequestDate().toLocalDate())
                .build();

        scheduleRepository.save(newSchedule);

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
    public ResponseDto<UpdateAllocationResponseDto> updateAllocation(Long allocationId, UpdateAllocationRequestDto dto, UserPrincipal userPrincipal) {
        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        if(!user.getRole().getName().equals(UserRole.ALLOCATIONS_MANAGER)){
            return ResponseDto.fail("FORBIDDEN", ResponseMessage.NO_PERMISSION);
        }

        Delivery delivery = deliveryRepository.findById(dto.getDeliveryId()).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));
        Assignment assignment = assignmentRepository.findById(dto.getAssignmentId()).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        Allocation allocation = allocationRepository.findById(allocationId).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        List<AllocationUpdateLog> logs = new ArrayList<>();

        if(!dto.getDeliveryId().equals(delivery.getId())) {
            logs.add(buildAllocationLog(allocation, user, username, "delivery_id", String.valueOf(allocation.getDelivery().getId()), String.valueOf(dto.getDeliveryId())));
            allocation.setDelivery(delivery);
        }

        if(!dto.getAssignmentId().equals(assignment.getId())) {
            logs.add(buildAllocationLog(allocation, user, username, "assignment_id", String.valueOf(allocation.getAssignment().getId()), String.valueOf(dto.getAssignmentId())));
            allocation.setAssignment(assignment);
        }

        if(!dto.getDistrictName().equals(allocation.getDistrictName())) {
            logs.add(buildAllocationLog(allocation, user, username, "district_name",  allocation.getDistrictName(), dto.getDistrictName()));
            allocation.setDistrictName(dto.getDistrictName());
        }

        Allocation updatedAllocation = allocationRepository.save(allocation);

        if(!logs.isEmpty()) {
            allocationUpdateLogRepository.saveAll(logs);
        }

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

    @Override
    public ResponseDto<UpdateAllocationResponseDto> updateAllocationStatus(Long allocationId, UpdateAllocationStatusRequestDto dto, UserPrincipal userPrincipal) {
        Allocation allocation = allocationRepository.findById(allocationId).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        if(!user.getRole().getName().equals(UserRole.ALLOCATIONS_MANAGER)){
            return ResponseDto.fail("FORBIDDEN", ResponseMessage.NO_PERMISSION);
        }

        AllocationStatus prevStatus = allocation.getStatus();

        if(dto.getStatus() != allocation.getStatus()) {
            allocation.setStatus(dto.getStatus());
        }

        Allocation updatedAllocation = allocationRepository.save(allocation);

        AllocationStatusLog allocationStatusLog = AllocationStatusLog.builder()
                .allocation(allocation)
                .user(user)
                .changedByUsername(username)
                .changeReason(dto.getChangeReason())
                .prevStatus(prevStatus)
                .newStatus(updatedAllocation.getStatus())
                .build();

        allocationStatusLogRepository.save(allocationStatusLog);

        UpdateAllocationResponseDto data = UpdateAllocationResponseDto.builder()
                .id(updatedAllocation.getId())
                .deliveryId(updatedAllocation.getDelivery().getId())
                .assignmentId(updatedAllocation.getAssignment().getId())
                .districtName(updatedAllocation.getDistrictName())
                .status(updatedAllocation.getStatus())
                .createdAt(updatedAllocation.getCreatedAt())
                .updatedAt(updatedAllocation.getUpdatedAt())
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    private AllocationUpdateLog buildAllocationLog(Allocation allocation, User user, String username, String type, String prevData, String newData) {
        return AllocationUpdateLog.builder()
                .allocation(allocation)
                .user(user)
                .changedByUsername(username)
                .type(type)
                .prevData(String.valueOf(prevData))
                .newData(String.valueOf(newData))
                .build();
    }

}
