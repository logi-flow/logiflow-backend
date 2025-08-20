package com.logi_flow.backend.dto.auth.request;

import com.logi_flow.backend.common.constants.Regex;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomerPasswordResetRequestDto {
    @NotBlank(message = "아이디는 필수 항목입니다.")
    @Pattern(regexp = Regex.USER_NAME, message = "아이디는 영문자로 시작하며, 총 5~12자 이내의 영문과 숫자 조합이어야 합니다.")
    private String username;

    @NotBlank(message = "사업자 번호는 필수 항목입니다.")
    @Pattern(regexp = Regex.BUSINESS_NUMBER, message = "사업자 번호는 000-00-00000 형식입니다.")
    private String businessNumber;

    @NotBlank(message = "대표자명은 필수 항목입니다.")
    @Pattern(regexp = Regex.NAME_KOREAN, message = "이름은 총 2~10자 이내의 한글이어야 합니다.")
    private String representativeName;

    @NotBlank(message = "이메일은 필수 항목입니다.")
    @Pattern(regexp = Regex.EMAIL, message = "example@mail.com 형식이어야 합니다.")
    private String email;
}
