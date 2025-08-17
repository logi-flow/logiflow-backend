package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.constants.ResponseCode;
import com.logi_flow.backend.common.constants.ResponseMessage;
import com.logi_flow.backend.common.enums.AllowanceTypeStatus;
import com.logi_flow.backend.common.enums.TableRef;
import com.logi_flow.backend.common.util.DateUtils;
import com.logi_flow.backend.common.util.SortUtils;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.allowanceType.request.CreateAllowanceTypeRequestDto;
import com.logi_flow.backend.dto.allowanceType.request.UpdateAllowanceTypeRequestDto;
import com.logi_flow.backend.dto.allowanceType.response.CreateAllowanceTypeResponseDto;
import com.logi_flow.backend.dto.allowanceType.response.GetAllAllowanceTypeResponseDto;
import com.logi_flow.backend.dto.allowanceType.response.GetAllowanceTypeDetailResponseDto;
import com.logi_flow.backend.dto.allowanceType.response.UpdateAllowanceTypeResponseDto;
import com.logi_flow.backend.entity.AllowanceType;
import com.logi_flow.backend.entity.AllowanceTypeUpdateLog;
import com.logi_flow.backend.entity.User;
import com.logi_flow.backend.repository.AllowanceTypeRepository;
import com.logi_flow.backend.repository.AllowanceTypeUpdateLogRepository;
import com.logi_flow.backend.repository.UserRepository;
import com.logi_flow.backend.service.AllowanceTypeService;
import com.logi_flow.backend.service.DeleteLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AllowanceTypeServiceImpl implements AllowanceTypeService {

    private final AllowanceTypeRepository allowanceTypeRepository;
    private final UserRepository userRepository;
    private final AllowanceTypeUpdateLogRepository allowanceTypeUpdateLogRepository;
    private final DeleteLogService deleteLogService;

    @Override
    @Transactional
    public ResponseDto<CreateAllowanceTypeResponseDto> createAllowanceType(CreateAllowanceTypeRequestDto dto) {
        CreateAllowanceTypeResponseDto data = null;

        String code = dto.getCode().trim().toUpperCase();

        if (allowanceTypeRepository.findByCodeAndStatus(code, AllowanceTypeStatus.ACTIVE).isPresent()) {
            return ResponseDto.fail(ResponseCode.EXISTS_TYPE_CODE, ResponseMessage.EXISTS_TYPE_CODE);
        }

        Optional<AllowanceType> deletedAllowanceType = allowanceTypeRepository.findByCodeAndStatus(code, AllowanceTypeStatus.DELETED);

        if (deletedAllowanceType.isPresent()) {
            AllowanceType restoreAllowanceType = deletedAllowanceType.get();
            restoreAllowanceType.setName(dto.getName());
            restoreAllowanceType.setDescription(dto.getDescription());
            restoreAllowanceType.setActive(true);
            restoreAllowanceType.setStatus(AllowanceTypeStatus.ACTIVE);
            allowanceTypeRepository.save(restoreAllowanceType);

            deleteLogService.removeIfExists(TableRef.ALLOWANCE_TYPE, restoreAllowanceType.getId());

            data = toCreateAllowanceTypeResponseDto(restoreAllowanceType);

            return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
        }

        AllowanceType newAllowanceType = AllowanceType.builder()
                .code(code)
                .name(dto.getName())
                .description(dto.getDescription())
                .isActive(true)
                .status(AllowanceTypeStatus.ACTIVE)
                .build();

        allowanceTypeRepository.save(newAllowanceType);

        data = toCreateAllowanceTypeResponseDto(newAllowanceType);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GetAllAllowanceTypeResponseDto> getAllAllowanceType(int page, int size, String sort) {
        Page<GetAllAllowanceTypeResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<AllowanceType> allowanceTypes = allowanceTypeRepository.findByStatus(AllowanceTypeStatus.ACTIVE, pageable);

        data = allowanceTypes.map(this::toGetAllAllowanceTypeResponseDto);

        return data;
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseDto<GetAllowanceTypeDetailResponseDto> getAllowanceTypeDetail(Long allowanceTypeId) {
        GetAllowanceTypeDetailResponseDto data = null;
        AllowanceType allowanceType = getAllowanceType(allowanceTypeId);

        data = GetAllowanceTypeDetailResponseDto.builder()
                .code(allowanceType.getCode())
                .name(allowanceType.getName())
                .description(allowanceType.getDescription())
                .isActive(allowanceType.isActive())
                .createdAt(DateUtils.format(allowanceType.getCreatedAt()))
                .updatedAt(DateUtils.format(allowanceType.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    @Transactional
    public ResponseDto<UpdateAllowanceTypeResponseDto> updateAllowanceType(UserPrincipal userPrincipal, Long allowanceTypeId, UpdateAllowanceTypeRequestDto dto) {
        UpdateAllowanceTypeResponseDto data = null;

        User user = userRepository.findByUsername(userPrincipal.getUsername())
                .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.USER_NOT_FOUND));

        AllowanceType savedAllowanceType = getAllowanceType(allowanceTypeId);

        if (!dto.getName().equals(savedAllowanceType.getName())) {
            String prev_data = savedAllowanceType.getName();
            savedAllowanceType.setName(dto.getName());
            createUpdateLog(user, savedAllowanceType, "name", prev_data, savedAllowanceType.getName());
        }

        if (!Objects.equals(dto.getDescription(), savedAllowanceType.getDescription())) {
            String prev_data = savedAllowanceType.getDescription();
            savedAllowanceType.setDescription(dto.getDescription());
            createUpdateLog(user, savedAllowanceType, "description", prev_data, savedAllowanceType.getDescription());
        }

        if (dto.isActive() != savedAllowanceType.isActive()) {
            String prev_data = String.valueOf(savedAllowanceType.isActive());
            savedAllowanceType.setActive(dto.isActive());
            createUpdateLog(user, savedAllowanceType, "is_active", prev_data, String.valueOf(savedAllowanceType.isActive()));
        }

        allowanceTypeRepository.save(savedAllowanceType);

        data = UpdateAllowanceTypeResponseDto.builder()
                .id(savedAllowanceType.getId())
                .code(savedAllowanceType.getCode())
                .name(savedAllowanceType.getName())
                .description(savedAllowanceType.getDescription())
                .isActive(savedAllowanceType.isActive())
                .status(savedAllowanceType.getStatus())
                .createdAt(DateUtils.format(savedAllowanceType.getCreatedAt()))
                .updatedAt(DateUtils.format(savedAllowanceType.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    @Transactional
    public ResponseDto<Void> deleteAllowanceType(UserPrincipal userPrincipal, Long allowanceTypeId) {
        User user = userRepository.findByUsername(userPrincipal.getUsername())
                .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.USER_NOT_FOUND));

        AllowanceType allowanceType = getAllowanceType(allowanceTypeId);

        if (allowanceType.getStatus() == AllowanceTypeStatus.DELETED) {
            return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        }

        allowanceType.setActive(false);
        allowanceType.setStatus(AllowanceTypeStatus.DELETED);
        allowanceTypeRepository.save(allowanceType);

        deleteLogService.createLog(TableRef.ALLOWANCE_TYPE, allowanceTypeId, user);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    private AllowanceType getAllowanceType(Long allowanceTypeId) {
        return allowanceTypeRepository.findById(allowanceTypeId)
                .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.RESOURCE_NOT_FOUND));
    }

    private CreateAllowanceTypeResponseDto toCreateAllowanceTypeResponseDto(AllowanceType allowanceType) {
        return CreateAllowanceTypeResponseDto.builder()
                .id(allowanceType.getId())
                .code(allowanceType.getCode())
                .name(allowanceType.getName())
                .description(allowanceType.getDescription())
                .isActive(allowanceType.isActive())
                .status(allowanceType.getStatus().name())
                .createdAt(DateUtils.format(allowanceType.getCreatedAt()))
                .updatedAt(DateUtils.format(allowanceType.getUpdatedAt()))
                .build();
    }

    private GetAllAllowanceTypeResponseDto toGetAllAllowanceTypeResponseDto(AllowanceType allowanceType) {
        return GetAllAllowanceTypeResponseDto.builder()
                .code(allowanceType.getCode())
                .name(allowanceType.getName())
                .isActive(allowanceType.isActive())
                .createdAt(DateUtils.format(allowanceType.getCreatedAt()))
                .updatedAt(DateUtils.format(allowanceType.getUpdatedAt()))
                .build();
    }

    private void createUpdateLog(User user, AllowanceType savedAllowanceType, String type, String prevData, String newData) {
        AllowanceTypeUpdateLog allowanceTypeUpdateLog = AllowanceTypeUpdateLog.builder()
                .allowanceType(savedAllowanceType)
                .user(user)
                .changedByUsername(user.getUsername())
                .type(type)
                .prevData(prevData)
                .newData(newData)
                .build();

        allowanceTypeUpdateLogRepository.save(allowanceTypeUpdateLog);
    }
}
