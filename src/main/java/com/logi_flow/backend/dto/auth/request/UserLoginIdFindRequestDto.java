package com.logi_flow.backend.dto.auth.request;

import com.logi_flow.backend.common.constants.Regex;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserLoginIdFindRequestDto {
    @NotBlank(message = "이름은 필수 항목입니다.")
    @Pattern(regexp = Regex.NAME_KOREAN, message = "이름은 총 2~10자 이내의 한글이어야 합니다.")
    private String name;

    @NotBlank(message = "휴대폰 번호는 필수 항목입니다.")
    @Pattern(regexp = Regex.PHONE_NUMBER, message = "휴대폰 번호는 010으로 사작하고 8자리여야 합니다.")
    private String phone;
}
