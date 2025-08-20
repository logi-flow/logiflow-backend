package com.logi_flow.backend.dto.employee.request;

import com.logi_flow.backend.common.constants.Regex;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateEmployeeRequestDto {
    @NotBlank(message = "휴대폰 번호는 필수 항목입니다.")
    @Pattern(regexp = Regex.PHONE_NUMBER, message = "휴대폰 번호는 000-0000-0000 형식이어야 합니다.")
    private String phoneNumber;

    @NotBlank(message = "우편 번호는 필수 항목입니다.")
    @Pattern(regexp = Regex.ZIPCODE, message = "우편 번호는 5자리 숫자여야 합니다.")
    private String zipcode;

    @NotBlank(message = "주소는 필수 항목입니다.")
    @Pattern(regexp = Regex.ADDRESS, message = "주소 형식이 올바르지 않습니다.")
    private String address;

    @Pattern(regexp = Regex.ADDRESS_DETAIL, message = "상세주소 형식이 올바르지 않습니다.")
    private String addressDetail;
}
