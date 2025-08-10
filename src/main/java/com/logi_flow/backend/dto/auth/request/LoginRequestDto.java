package com.logi_flow.backend.dto.auth.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginRequestDto {
    @NotBlank(message = "아이디는 필수 항목입니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    private String password;
}
