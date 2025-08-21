package com.logi_flow.backend.dto.customer.request;

import com.logi_flow.backend.common.constants.Regex;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateCustomerAdminRequestDto {
    @NotBlank(message = "사업자 번호는 필수 항목입니다.")
    @Pattern(regexp = Regex.BUSINESS_NUMBER, message = "사업자 번호는 00-000-00000 형식입니다.")
    private String businessNumber;

    @NotBlank(message = "사업장명은 필수 항목입니다.")
    @Pattern(regexp = Regex.BUSINESS, message = "사업장명은 한글, 영문, 숫자, 공백, 특수문자(·, &, /, -, 괄호)만 사용가능합니다.")
    private String name;

    @NotBlank(message = "대표자명은 필수 항목입니다.")
    @Pattern(regexp = Regex.NAME_KOREAN, message = "이름은 총 2~10자 이내의 한글이어야 합니다.")
    private String representativeName;

    @NotBlank(message = "업태는 필수 항목입니다.")
    @Pattern(regexp = Regex.BUSINESS, message = "업태는 한글, 영문, 숫자, 공백, 특수문자(·, &, /, -, 괄호)만 사용가능합니다.")
    private String businessType;

    @NotBlank(message = "업종은 필수 항목입니다.")
    @Pattern(regexp = Regex.BUSINESS, message = "업종은 한글, 영문, 숫자, 공백, 특수문자(·, &, /, -, 괄호)만 사용가능합니다.")
    private String businessItems;

    @NotBlank(message = "전화번호는 필수 항목입니다.")
    @Pattern(regexp = Regex.TELEPHONE, message = "전화번호는 000-000-0000 형식이거나 010-0000-0000 형식입니다.")
    private String telephone;

    @Pattern(regexp = Regex.FAX, message = "팩스번호 형식이 올바르지 않습니다. 예: 02-123-4567")
    private String fax;

    @NotBlank(message = "사업장 우편 번호는 필수 항목입니다.")
    @Pattern(regexp = Regex.ZIPCODE, message = "우편 번호는 5자리 숫자여야 합니다.")
    private String businessZipCode;

    @NotBlank(message = "사업장 주소는 필수 항목입니다.")
    @Pattern(regexp = Regex.ADDRESS, message = "주소 형식이 올바르지 않습니다.")
    private String businessAddress;

    @Pattern(regexp = Regex.ADDRESS_DETAIL, message = "상세주소 형식이 올바르지 않습니다.")
    private String businessAddressDetail;

    @Pattern(regexp = Regex.BUSINESS, message = "대표자 직책은 한글, 영문, 숫자, 공백, 특수문자(·, &, /, -, 괄호)만 사용가능합니다.")
    private String chargePosition;

    @Pattern(regexp = Regex.BUSINESS, message = "대표자 부서는 한글, 영문, 숫자, 공백, 특수문자(·, &, /, -, 괄호)만 사용가능합니다.")
    private String chargeDepartment;

    @Pattern(regexp = Regex.NAME_KOREAN, message = "이름은 총 2~10자 이내의 한글이어야 합니다.")
    private String chargeName;

    @Pattern(regexp = Regex.PHONE_NUMBER, message = "휴대폰 번호는 000-0000-0000 형식이어야 합니다.")
    private String chargePhone;

    @Pattern(regexp = Regex.EMAIL, message = "example@mail.com 형식이어야 합니다.")
    private String chargeEmail;
}
