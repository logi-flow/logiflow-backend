package com.logi_flow.backend.dto.auth.request;

import com.logi_flow.backend.common.constants.Regex;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomerLoginIdFindRequestDto {
    @NotBlank(message = "사업자 번호는 필수 항목입니다.")
    @Pattern(regexp = Regex.BUSINESS_NUMBER, message = "사업자 번호는 00-000-00000 형식입니다.")
    private String businessNumber;

    @NotBlank(message = "대표자명은 필수 항목입니다.")
    @Pattern(regexp = Regex.NAME_KOREAN, message = "이름은 총 2~10자 이내의 한글이어야 합니다.")
    private String representativeName;

    @NotBlank(message = "이메일은 필수 항목입니다.")
    @Pattern(regexp = Regex.EMAIL, message = "example@mail.com 형식이어야 합니다.")
    private String email;
}
