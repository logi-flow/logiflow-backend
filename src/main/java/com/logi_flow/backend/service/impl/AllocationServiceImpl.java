package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.constants.ResponseCode;
import com.logi_flow.backend.common.constants.ResponseMessage;
import com.logi_flow.backend.common.enums.AllocationStatus;
import com.logi_flow.backend.common.enums.user.UserRole;
import com.logi_flow.backend.common.util.DateUtils;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.allocation.request.CreateAllocationRequestDto;
import com.logi_flow.backend.dto.allocation.request.UpdateAllocationRequestDto;
import com.logi_flow.backend.dto.allocation.request.UpdateAllocationStatusRequestDto;
import com.logi_flow.backend.dto.allocation.response.CreateAllocationResponseDto;
import com.logi_flow.backend.dto.allocation.response.UpdateAllocationResponseDto;
import com.logi_flow.backend.entity.*;
import com.logi_flow.backend.repository.*;
import com.logi_flow.backend.service.AlertService;
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
    private final ReturnDeliveryRepository returnDeliveryRepository;

    private final AllocationUpdateLogRepository allocationUpdateLogRepository;
    private final AllocationStatusLogRepository allocationStatusLogRepository;

    private final AlertService alertService;

    @Override
    public ResponseDto<CreateAllocationResponseDto> createAllocation(CreateAllocationRequestDto dto, UserPrincipal userPrincipal) {
        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        if(!user.getRole().getName().equals(UserRole.ALLOCATIONS_MANAGER)){
            return ResponseDto.fail("FORBIDDEN", ResponseMessage.NO_PERMISSION);
        }

        Delivery delivery = deliveryRepository.findById(dto.getDeliveryId()).orElse(null);
        ReturnDelivery returnDelivery = (delivery == null) ? returnDeliveryRepository.findById(dto.getReturnDeliveryId()).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND)) : null;

        Assignment assignment = assignmentRepository.findById(dto.getAssignmentId()).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        Allocation newAllocation = Allocation.builder()
                .delivery(delivery)
                .returnDelivery(returnDelivery)
                .assignment(assignment)
                .districtName(dto.getDistrictName())
                .status(dto.getStatus())
                .build();

        allocationRepository.save(newAllocation);

        Schedule newSchedule = Schedule.builder()
                .allocation(newAllocation)
                .allocationDate((delivery != null ? delivery.getRequestDate().toLocalDate() : returnDelivery.getRequestDate()))
                .build();

        scheduleRepository.save(newSchedule);

        String alertMessage;
        Long sendUserId;

        if (delivery != null) {
            alertMessage = "주문번호 #" + delivery.getId() + " 배차가 완료되었습니다.";
            sendUserId = delivery.getCustomer().getUser().getId();
        } else {
            alertMessage = "반품 주문번호 #" + returnDelivery.getId() + " 배차가 완료되었습니다.";
            sendUserId = returnDelivery.getDelivery().getCustomer().getUser().getId();
        }

        alertService.sendToUser(sendUserId, alertMessage);

        CreateAllocationResponseDto data = CreateAllocationResponseDto.builder()
                .id(newAllocation.getId())
                .deliveryId(delivery != null ? delivery.getId() : null)
                .returnDeliveryId(returnDelivery != null ? returnDelivery.getId() : null)
                .assignmentId(newAllocation.getId())
                .districtName(newAllocation.getDistrictName())
                .status(newAllocation.getStatus())
                .createdAt(DateUtils.format(newAllocation.getCreatedAt()))
                .updatedAt(DateUtils.format(newAllocation.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<UpdateAllocationResponseDto> updateAllocation(Long allocationId, UpdateAllocationRequestDto dto, UserPrincipal userPrincipal) {
        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        if(!user.getRole().getName().equals(UserRole.ALLOCATIONS_MANAGER)) {
            return ResponseDto.fail("FORBIDDEN", ResponseMessage.NO_PERMISSION);
        }

        Allocation allocation = allocationRepository.findById(allocationId).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        List<AllocationUpdateLog> logs = new ArrayList<>();

        if(dto.getDeliveryId() != null && dto.getReturnDeliveryId() != null) {
            throw new IllegalArgumentException("배차 하나에는 deliveryId, returnDeliveryId 중에 하나만 지정 가능.");
        }

        if(dto.getDeliveryId() == null && dto.getReturnDeliveryId() == null) {
            throw new IllegalArgumentException("deliveryId, returnDeliveryId 중에 하나는 포함해야 함.");
        }

        if(dto.getDeliveryId() != null) {
            Delivery delivery = deliveryRepository.findById(dto.getDeliveryId()).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

            if(!dto.getDeliveryId().equals(delivery.getId())) {
                logs.add(buildAllocationLog(allocation, user, username, "delivery_id", String.valueOf(allocation.getDelivery().getId()), String.valueOf(dto.getDeliveryId())));
                allocation.setDelivery(delivery);
            }
        }

        if(dto.getReturnDeliveryId() != null) {
            ReturnDelivery returnDelivery = returnDeliveryRepository.findById(dto.getReturnDeliveryId()).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

            if(!dto.getReturnDeliveryId().equals(returnDelivery.getId())) {
                logs.add(buildAllocationLog(allocation, user, username, "return_delivery_id",  String.valueOf(allocation.getReturnDelivery().getId()), String.valueOf(dto.getReturnDeliveryId())));
                allocation.setReturnDelivery(returnDelivery);
            }
        }

        Assignment assignment = assignmentRepository.findById(dto.getAssignmentId()).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

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

        String alertMessage;
        Long sendUserId;

        if (updatedAllocation.getDelivery() != null) {
            alertMessage = "주문번호 #" + updatedAllocation.getDelivery().getId() + " 배차 정보가 수정되었습니다.";
            sendUserId = updatedAllocation.getDelivery().getCustomer().getUser().getId();
        } else {
            alertMessage = "반품 주문번호 #" + updatedAllocation.getReturnDelivery().getId() + " 배차 정보가 수정되었습니다.";
            sendUserId = updatedAllocation.getReturnDelivery().getDelivery().getCustomer().getUser().getId();
        }

        alertService.sendToUser(sendUserId, alertMessage);

        UpdateAllocationResponseDto data = UpdateAllocationResponseDto.builder()
                .id(updatedAllocation.getId())
                .deliveryId(updatedAllocation.getDelivery() != null ? updatedAllocation.getDelivery().getId() : null)
                .returnDeliveryId(updatedAllocation.getReturnDelivery() != null ? updatedAllocation.getReturnDelivery().getId() : null)
                .assignmentId(updatedAllocation.getId())
                .districtName(updatedAllocation.getDistrictName())
                .status(updatedAllocation.getStatus())
                .createdAt(DateUtils.format(updatedAllocation.getCreatedAt()))
                .updatedAt(DateUtils.format(updatedAllocation.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<UpdateAllocationResponseDto> updateAllocationStatus(Long allocationId, UpdateAllocationStatusRequestDto dto, UserPrincipal userPrincipal) {
        Allocation allocation = allocationRepository.findById(allocationId).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.RESOURCE_NOT_FOUND));

        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        if(!user.getRole().getName().equals(UserRole.ALLOCATIONS_MANAGER)) {
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

        String alertMessage;
        Long sendUserId;

        if (updatedAllocation.getDelivery() != null) {
            alertMessage = "주문번호 #" + updatedAllocation.getDelivery().getId() + " 배차 상태가 업데이트 되었습니다.";
            sendUserId = updatedAllocation.getDelivery().getCustomer().getUser().getId();
        } else {
            alertMessage = "반품 주문번호 #" + updatedAllocation.getReturnDelivery().getId() + " 배차 상태가 업데이트 되었습니다.";
            sendUserId = updatedAllocation.getReturnDelivery().getDelivery().getCustomer().getUser().getId();
        }

        alertService.sendToUser(sendUserId, alertMessage);

        UpdateAllocationResponseDto data = UpdateAllocationResponseDto.builder()
                .id(updatedAllocation.getId())
                .deliveryId(updatedAllocation.getDelivery().getId())
                .assignmentId(updatedAllocation.getAssignment().getId())
                .districtName(updatedAllocation.getDistrictName())
                .status(updatedAllocation.getStatus())
                .createdAt(DateUtils.format(updatedAllocation.getCreatedAt()))
                .updatedAt(DateUtils.format(updatedAllocation.getUpdatedAt()))
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
