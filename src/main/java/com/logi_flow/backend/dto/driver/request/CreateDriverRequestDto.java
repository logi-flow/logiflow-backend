package com.logi_flow.backend.dto.driver.request;

import com.logi_flow.backend.common.enums.driver.DriverDistrict;
import com.logi_flow.backend.common.enums.driver.DriverStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class CreateDriverRequestDto {
    @NotNull(message = "상태 입력은 필수 항목입니다.")
    private DriverStatus status;

    @NotBlank(message = "이름은 필수 항목입니다.")
    private String name;

    @NotBlank(message = "주민등록번호는 필수 항목입니다.")
    private String identityNumber;

    @NotBlank(message = "휴대폰 번호는 필수 항목입니다.")
    private String phoneNumber;

    @NotBlank(message = "주소는 필수 항목입니다.")
    private String zipcode;

    @NotBlank(message = "상세주소는 필수 항목입니다.")
    private String address;
    private String addressDetail;

    @NotNull (message = "담당구역은 필수 항목입니다.")
    private DriverDistrict district;

    @NotNull (message = "급여는 필수 항목입니다.")
    private int pay;

    @NotNull (message = "입사일은 필수 항목입니다.")
    private LocalDate companyJoin;
}
