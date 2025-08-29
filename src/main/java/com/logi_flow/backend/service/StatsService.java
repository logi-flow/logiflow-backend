package com.logi_flow.backend.service;

import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.stats.driverJoinLeave.response.GetDriverJoinLeaveResponseDto;

import java.time.YearMonth;

public interface StatsService {
    ResponseDto<GetDriverJoinLeaveResponseDto> getDriverJoinLeave(YearMonth from, YearMonth to);
}
