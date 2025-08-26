package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.util.DateUtils;
import com.logi_flow.backend.common.util.SortUtils;
import com.logi_flow.backend.dto.contractLog.response.GetContractStatusLogResponseDto;
import com.logi_flow.backend.dto.contractLog.response.GetContractUpdateLogResponseDto;
import com.logi_flow.backend.entity.ContractStatusLog;
import com.logi_flow.backend.entity.ContractUpdateLog;
import com.logi_flow.backend.repository.ContractStatusLogRepository;
import com.logi_flow.backend.repository.ContractUpdateLogRepository;
import com.logi_flow.backend.service.ContractLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ContractLogServiceImpl implements ContractLogService {

    private final ContractUpdateLogRepository contractUpdateLogRepository;
    private final ContractStatusLogRepository contractStatusLogRepository;

    @Override
    public Page<GetContractUpdateLogResponseDto> getContractUpdateLog(int page, int size, String sort) {

        Page<GetContractUpdateLogResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<ContractUpdateLog> contractUpdateLogs = contractUpdateLogRepository.findAll(pageable);

        data = contractUpdateLogs.map(this::toGetContractUpdateLogResponseDto);
        return data;
    }

    @Override
    public Page<GetContractStatusLogResponseDto> getContractStatusLog(int page, int size, String sort) {

        Page<GetContractStatusLogResponseDto> data = null;

        Pageable pageable = PageRequest.of(page, size, SortUtils.parseCreatedAtSort(sort));
        Page<ContractStatusLog> contractStatusLogs = contractStatusLogRepository.findAll(pageable);

        data = contractStatusLogs.map(this::toGetContractStatusLogResponseDto);
        return data;
    }


    private GetContractUpdateLogResponseDto toGetContractUpdateLogResponseDto(ContractUpdateLog contractUpdateLog) {
        return GetContractUpdateLogResponseDto.builder()
                .customerName(contractUpdateLog.getContract().getCustomer().getName())
                .businessNumber(contractUpdateLog.getContract().getCustomer().getBusinessNumber())
                .representativeName(contractUpdateLog.getContract().getCustomer().getRepresentativeName())
                .changedByUsername(contractUpdateLog.getChangedByUsername())
                .type(contractUpdateLog.getType())
                .prevData(contractUpdateLog.getPrevData())
                .newData(contractUpdateLog.getNewData())
                .createdAt(DateUtils.format(contractUpdateLog.getCreatedAt()))
                .build();
    }

    private GetContractStatusLogResponseDto toGetContractStatusLogResponseDto(ContractStatusLog contractStatusLog) {
        return GetContractStatusLogResponseDto.builder()
                .customerName(contractStatusLog.getContract().getCustomer().getName())
                .businessNumber(contractStatusLog.getContract().getCustomer().getBusinessNumber())
                .representativeName(contractStatusLog.getContract().getCustomer().getRepresentativeName())
                .changedByUsername(contractStatusLog.getChangedByUsername())
                .changeReason(contractStatusLog.getChangeReason())
                .prevStatus(contractStatusLog.getPrevStatus())
                .newStatus(contractStatusLog.getNewStatus())
                .createdAt(DateUtils.format(contractStatusLog.getCreatedAt()))
                .build();
    }

}
