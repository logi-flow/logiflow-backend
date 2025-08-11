package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.auth.request.*;
import com.logi_flow.backend.dto.auth.response.*;
import com.logi_flow.backend.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    @Override
    public ResponseDto<CustomerSignUpResponseDto> signup(CustomerSignUpRequestDto dto) {
        return null;
    }

    @Override
    public ResponseDto<LoginResponseDto> login(LoginRequestDto dto) {
        return null;
    }

    @Override
    public ResponseDto<UsernameCheckResponseDto> checkLoginIdDuplicate(String username) {
        return null;
    }

    @Override
    public ResponseDto<EmailCheckResponseDto> checkEmailDuplicate(String email) {
        return null;
    }

    @Override
    public ResponseDto<BusinessNumberCheckResponseDto> checkBusinessNumberDuplicate(String businessNumber) {
        return null;
    }

    @Override
    public ResponseDto<LogoutResponseDto> logout(HttpServletResponse response) {
        return null;
    }

    @Override
    public ResponseDto<CustomerLoginIdFindResponseDto> findCustomerLoginId(CustomerLoginIdFindRequestDto dto) {
        return null;
    }

    @Override
    public ResponseDto<UserLoginIdFindResponseDto> findUserLoginId(UserLoginIdFindRequestDto dto) {
        return null;
    }

    @Override
    public ResponseDto<CustomerPasswordResetResponseDto> getPasswordResetCustomer(CustomerPasswordResetRequestDto dto) {
        return null;
    }

    @Override
    public ResponseDto<UserPasswordResetResponseDto> getPasswordResetUser(UserPasswordResetRequestDto dto) {
        return null;
    }

    @Override
    public ResponseDto<PasswordResetSendEmailResponseDto> resetPassword(PasswordResetRequestDto dto) {
        return null;
    }

    @Override
    public ResponseDto<PasswordResetSendEmailResponseDto> requestResetPasswordEmail(PasswordResetSendEmailRequestDto dto) {
        return null;
    }

    @Override
    public ResponseDto<PasswordResetSendEmailResponseDto> verifyEmail(String token) {
        return null;
    }
}
