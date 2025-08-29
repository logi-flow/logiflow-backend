package com.logi_flow.backend.controller;

import com.logi_flow.backend.common.constants.ApiMappingPattern;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.auth.request.*;
import com.logi_flow.backend.dto.auth.response.*;
import com.logi_flow.backend.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.AUTH_API)
public class AuthController {

    private final AuthService authService;

    private static final String SIGNUP_API = "/signup";
    private static final String LOGIN_API = "/login";
    private static final String EXIST_ID_API = "/login-id/exist";
    private static final String EXIST_EMAIL_API = "/email/exist";
    private static final String EXIST_BUSINESS_NUMBER_API = "/business-number/exist";
    private static final String CUSTOMER_FIND_ID_API = "/login-id/find/customers";
    private static final String USER_FIND_ID_API = "/login-id/find/users";
    private static final String CUSTOMER_RESET_PASSWORD_API = "/password/reset/customers";
    private static final String USER_RESET_PASSWORD_API = "/password/reset/users";
    private static final String RESET_PASSWORD_API = "/password/reset";
    private static final String MUST_CHANGE_PASSWORD_API = "/password/first-change";
    private static final String VERIFY_EMAIL_API = "/email/verify";

    @PostMapping(SIGNUP_API)
    public ResponseEntity<ResponseDto<CustomerSignUpResponseDto>> signup(
            @Valid @RequestPart(value = "dto") CustomerSignUpRequestDto dto,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage
    ) {
        ResponseDto<CustomerSignUpResponseDto> response = authService.signup(dto, profileImage);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    @PostMapping(LOGIN_API)
    public ResponseEntity<ResponseDto<LoginResponseDto>> login(
            @Valid @RequestBody LoginRequestDto dto
    ) {
        ResponseDto<LoginResponseDto> response = authService.login(dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping(EXIST_ID_API)
    public ResponseEntity<ResponseDto<UsernameCheckResponseDto>> checkLoginIdDuplicate(
            @RequestParam String username
    ) {
        ResponseDto<UsernameCheckResponseDto> response = authService.checkLoginIdDuplicate(username);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping(EXIST_EMAIL_API)
    public ResponseEntity<ResponseDto<EmailCheckResponseDto>> checkEmailDuplicate(
            @RequestParam String email
    ) {
        ResponseDto<EmailCheckResponseDto> response = authService.checkEmailDuplicate(email);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping(EXIST_BUSINESS_NUMBER_API)
    public ResponseEntity<ResponseDto<BusinessNumberCheckResponseDto>> checkBusinessNumberDuplicate(
            @RequestParam String businessNumber
    ) {
        ResponseDto<BusinessNumberCheckResponseDto> response = authService.checkBusinessNumberDuplicate(businessNumber);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @PostMapping(CUSTOMER_FIND_ID_API)
    public ResponseEntity<ResponseDto<CustomerLoginIdFindResponseDto>> findCustomerLoginId(
            @Valid @RequestBody CustomerLoginIdFindRequestDto dto
    ) {
        ResponseDto<CustomerLoginIdFindResponseDto> response = authService.findCustomerLoginId(dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @PostMapping(USER_FIND_ID_API)
    public ResponseEntity<ResponseDto<UserLoginIdFindResponseDto>> findUserLoginId(
            @Valid @RequestBody UserLoginIdFindRequestDto dto
    ) {
        ResponseDto<UserLoginIdFindResponseDto> response = authService.findUserLoginId(dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @PostMapping(CUSTOMER_RESET_PASSWORD_API)
    public ResponseEntity<ResponseDto<CustomerPasswordResetResponseDto>> requestPasswordResetCustomer(
            @Valid @RequestBody CustomerPasswordResetRequestDto dto
    ) {
        ResponseDto<CustomerPasswordResetResponseDto> response = authService.requestPasswordResetCustomer(dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @PostMapping(USER_RESET_PASSWORD_API)
    public ResponseEntity<ResponseDto<UserPasswordResetResponseDto>> requestPasswordResetUser(
            @Valid @RequestBody UserPasswordResetRequestDto dto
    ) {
        ResponseDto<UserPasswordResetResponseDto> response = authService.requestPasswordResetUser(dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @PostMapping(RESET_PASSWORD_API)
    public ResponseEntity<ResponseDto<PasswordResetSendEmailResponseDto>> resetPassword(
            @RequestParam String token,
            @Valid @RequestBody PasswordResetRequestDto dto
    ) {
        ResponseDto<PasswordResetSendEmailResponseDto> response = authService.resetPassword(token, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @PostMapping(MUST_CHANGE_PASSWORD_API)
    public ResponseEntity<ResponseDto<FirstPasswordChangeResponseDto>> firstChange(
            @Valid @RequestBody FirstPasswordChangeRequestDto dto
    ) {
        ResponseDto<FirstPasswordChangeResponseDto> response = authService.firstChange(dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping(VERIFY_EMAIL_API)
    public ResponseEntity<ResponseDto<PasswordResetSendEmailResponseDto>> verifyEmail(
            @RequestParam String token
    ) {
        ResponseDto<PasswordResetSendEmailResponseDto> response = authService.verifyEmail(token);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }
}
