package com.logi_flow.backend.dto.admin.request;

import com.logi_flow.backend.common.constants.Regex;
import com.logi_flow.backend.common.enums.user.UserRole;
import com.logi_flow.backend.common.enums.user.UserStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateAdminUserRequestDto {
    @NotBlank(message = "아이디는 필수 항목입니다.")
    @Pattern(regexp = Regex.USER_NAME, message = "아이디는 영문자로 시작하며, 총 5~12자 이내의 영문과 숫자 조합이어야 합니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    @Pattern(regexp = Regex.PASSWORD, message = "비밀번호는 영문/숫자/특수문자(~!@#$%^&*()-_=+)를 포함한 8~15자 이내이어야 합니다.")
    private String password;

    @NotNull
    private UserRole role;

    @NotNull
    private UserStatus status;
}
