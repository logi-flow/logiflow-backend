package com.logi_flow.backend.service;

import com.logi_flow.backend.dto.retunDeliveryLog.response.GetAllReturnDeliveryStatusLogResponseDto;
import com.logi_flow.backend.dto.retunDeliveryLog.response.GetAllReturnDeliveryUpdateLogResponseDto;
import org.springframework.data.domain.Page;

public interface ReturnDeliveryLogService {

    Page<GetAllReturnDeliveryUpdateLogResponseDto> getAllReturnDeliveryUpdateLogs(int page, int size, String sort);

    Page<GetAllReturnDeliveryStatusLogResponseDto> getAllReturnDeliveryStatusLogs(int page, int size, String sort);
}
