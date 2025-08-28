package com.logi_flow.backend.dto.stats.driverJoinLeave.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class GetDriverJoinLeaveResponseDto {
    private String from;
    private String to;
    private List<DriverJoinLeavePoint> points;
}
