package com.logi_flow.backend.service;

import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.auth.request.*;
import com.logi_flow.backend.dto.auth.response.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

public interface AuthService {
    ResponseDto<CustomerSignUpResponseDto> signup(@Valid CustomerSignUpRequestDto dto);

    ResponseDto<LoginResponseDto> login(@Valid LoginRequestDto dto);

    ResponseDto<UsernameCheckResponseDto> checkLoginIdDuplicate(String username);

    ResponseDto<EmailCheckResponseDto> checkEmailDuplicate(String email);

    ResponseDto<BusinessNumberCheckResponseDto> checkBusinessNumberDuplicate(String businessNumber);

    ResponseDto<LogoutResponseDto> logout(HttpServletResponse response);

    ResponseDto<CustomerLoginIdFindResponseDto> findCustomerLoginId(@Valid CustomerLoginIdFindRequestDto dto);

    ResponseDto<UserLoginIdFindResponseDto> findUserLoginId(@Valid UserLoginIdFindRequestDto dto);

    ResponseDto<CustomerPasswordResetResponseDto> getPasswordResetCustomer(@Valid CustomerPasswordResetRequestDto dto);

    ResponseDto<UserPasswordResetResponseDto> getPasswordResetUser(@Valid UserPasswordResetRequestDto dto);

    ResponseDto<PasswordResetSendEmailResponseDto> resetPassword(@Valid PasswordResetRequestDto dto);

    ResponseDto<PasswordResetSendEmailResponseDto> requestResetPasswordEmail(@Valid PasswordResetSendEmailRequestDto dto);

    ResponseDto<PasswordResetSendEmailResponseDto> verifyEmail(String token);
}
