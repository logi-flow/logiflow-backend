package com.logi_flow.backend.dto.driver.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateDriverRequestDto {
    @NotBlank(message = "이름은 필수 항목입니다.")
    private String name;

    @NotBlank(message = "휴대폰 번호는 필수 항목입니다.")
    private String phoneNumber;

    @NotBlank(message = "우편번호는 필수 항목입니다.")
    private String zipcode;

    @NotBlank(message = "상세주소는 필수 항목입니다.")
    private String address;
    private String addressDetail;
}
