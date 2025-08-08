package com.logi_flow.backend.dto.driver.request;

import com.logi_flow.backend.common.enums.driver.DriverDistrict;
import com.logi_flow.backend.common.enums.driver.DriverStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class UpdateDriverRequestDto {
    private DriverStatus status;
    private String name;
    private String identityNumber;
    private String phoneNumber;
    private String zipcode;
    private String address;
    private String addressDetail;
    private DriverDistrict district;
    private int pay;
    private LocalDate companyJoin;
}
