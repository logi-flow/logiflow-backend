package com.logi_flow.backend.dto.employee.request;

import com.logi_flow.backend.common.constants.Regex;
import com.logi_flow.backend.common.enums.employee.Department;
import com.logi_flow.backend.common.enums.employee.Position;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class UpdateEmployeeAdminRequestDto {
    @NotBlank(message = "이름은 필수 항목입니다.")
    @Pattern(regexp = Regex.NAME_KOREAN, message = "이름은 총 2~10자 이내의 한글이어야 합니다.")
    private String name;

    @NotBlank(message = "주민등록번호는 필수 항목입니다.")
    @Pattern(regexp = Regex.IDENTITY_NUMBER, message = "주민등록번호 형식이 올바르지 않습니다.")
    private String identityNumber;

    @NotBlank(message = "휴대폰 번호는 필수 항목입니다.")
    @Pattern(regexp = Regex.PHONE_NUMBER, message = "휴대폰 번호는 000-0000-0000 형식이어야 합니다.")
    private String phone;

    @NotBlank(message = "우편 번호는 필수 항목입니다.")
    @Pattern(regexp = Regex.ZIPCODE, message = "우편 번호는 5자리 숫자여야 합니다.")
    private String zipcode;

    @NotBlank(message = "주소는 필수 항목입니다.")
    @Pattern(regexp = Regex.ADDRESS, message = "주소 형식이 올바르지 않습니다.")
    private String address;

    @Pattern(regexp = Regex.ADDRESS_DETAIL, message = "상세주소 형식이 올바르지 않습니다.")
    private String addressDetail;

    @NotNull(message = "부서는 필수 항목입니다.")
    private Department department;

    @NotNull(message = "직급은 필수 항목입니다.")
    private Position position;

    @NotNull(message = "입사일은 필수 항목입니다.")
    private LocalDate companyJoin;
}
