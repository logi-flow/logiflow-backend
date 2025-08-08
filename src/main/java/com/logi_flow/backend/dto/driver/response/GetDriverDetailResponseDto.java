package com.logi_flow.backend.dto.driver.response;

import com.logi_flow.backend.common.enums.driver.DriverStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class GetDriverDetailResponseDto {
    private Long driverId;
    private String username;
    private String name;
    private DriverStatus status;
    private String PhoneNumber;
    private String vehicleNumber;
    private String identityNumber;
    private String zipcode;
    private String address;
    private String addressDetail;
    private String district;
    private int pay;
    private String companyJoin;
    private String driverLicenseName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
