package com.logi_flow.backend.service;

import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.auth.request.*;
import com.logi_flow.backend.dto.auth.response.*;
import com.logi_flow.backend.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

public interface AuthService {
    ResponseDto<CustomerSignUpResponseDto> signup(@Valid CustomerSignUpRequestDto dto,  MultipartFile profileImage);

    ResponseDto<LoginResponseDto> login(@Valid LoginRequestDto dto);

    ResponseDto<UsernameCheckResponseDto> checkLoginIdDuplicate(String username);

    ResponseDto<EmailCheckResponseDto> checkEmailDuplicate(String email);

    ResponseDto<BusinessNumberCheckResponseDto> checkBusinessNumberDuplicate(String businessNumber);

    ResponseDto<CustomerLoginIdFindResponseDto> findCustomerLoginId(@Valid CustomerLoginIdFindRequestDto dto);

    ResponseDto<UserLoginIdFindResponseDto> findUserLoginId(@Valid UserLoginIdFindRequestDto dto);

    ResponseDto<CustomerPasswordResetResponseDto> requestPasswordResetCustomer(@Valid CustomerPasswordResetRequestDto dto);

    ResponseDto<UserPasswordResetResponseDto> requestPasswordResetUser(@Valid UserPasswordResetRequestDto dto);

    ResponseDto<PasswordResetSendEmailResponseDto> resetPassword(String token, @Valid PasswordResetRequestDto dto);

    ResponseDto<PasswordResetSendEmailResponseDto> verifyEmail(String token);

    boolean checkPassword(User user, String password);

    ResponseDto<FirstPasswordChangeResponseDto> firstChange(@Valid FirstPasswordChangeRequestDto dto);

    boolean checkEmail(String email);
}
