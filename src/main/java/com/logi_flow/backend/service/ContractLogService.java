package com.logi_flow.backend.service;

import com.logi_flow.backend.dto.contractLog.response.GetContractStatusLogResponseDto;
import com.logi_flow.backend.dto.contractLog.response.GetContractUpdateLogResponseDto;
import org.springframework.data.domain.Page;

public interface ContractLogService {
    Page<GetContractUpdateLogResponseDto> getContractUpdateLog(int page, int size, String sort);

    Page<GetContractStatusLogResponseDto> getContractStatusLog(int page, int size, String sort);
}
