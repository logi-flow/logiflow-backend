package com.logi_flow.backend.dto.driver.response;

import com.logi_flow.backend.common.enums.driver.DriverDistrict;
import com.logi_flow.backend.common.enums.driver.DriverLicenseType;
import com.logi_flow.backend.common.enums.driver.DriverStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class GetDriverDetailResponseDto {
    private Long driverId;
    private String name;
    private String username;
    private DriverStatus status;
    private String PhoneNumber;
    private String identityNumber;
    private String zipcode;
    private String address;
    private String addressDetail;
    private DriverDistrict district;
    private Integer pay;
    private LocalDate companyJoin;
    private DriverLicenseType driverType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
