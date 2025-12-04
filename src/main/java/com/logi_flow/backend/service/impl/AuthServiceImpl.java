package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.constants.ResponseCode;
import com.logi_flow.backend.common.constants.ResponseMessage;
import com.logi_flow.backend.common.enums.CustomerStatus;
import com.logi_flow.backend.common.enums.user.UserRole;
import com.logi_flow.backend.common.enums.user.UserStatus;
import com.logi_flow.backend.common.util.DateUtils;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.auth.request.*;
import com.logi_flow.backend.dto.auth.response.*;
import com.logi_flow.backend.entity.*;
import com.logi_flow.backend.provider.JwtProvider;
import com.logi_flow.backend.repository.*;
import com.logi_flow.backend.service.AlertService;
import com.logi_flow.backend.service.AuthService;
import com.logi_flow.backend.service.MailService;
import com.logi_flow.backend.service.UploadFileService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;
    private final EmployeeRepository employeeRepository;
    private final DriverRepository driverRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final UploadFileService uploadFileService;
    private final AlertService alertService;

    @Override
    @Transactional
    public ResponseDto<CustomerSignUpResponseDto> signup(CustomerSignUpRequestDto dto, MultipartFile profileImage) {
        CustomerSignUpResponseDto data = null;

        String username = dto.getUsername().trim();
        String password = dto.getPassword();
        String confirmPassword = dto.getConfirmPassword();
        String businessNumber = dto.getBusinessNumber();
        String email = dto.getEmail().trim().toLowerCase();

        if (!password.equals(confirmPassword)) {
            return ResponseDto.fail(ResponseCode.NOT_MATCH_PASSWORD, ResponseMessage.NOT_MATCH_PASSWORD);
        }

        if (userRepository.existsByUsername(username)) {
            return ResponseDto.fail(ResponseCode.USER_ALREADY_EXISTS, ResponseMessage.USER_ALREADY_EXISTS);
        }

        if (userRepository.existsByEmail(email)) {
            return ResponseDto.fail(ResponseCode.USER_ALREADY_EXISTS, ResponseMessage.USER_ALREADY_EXISTS);
        }

        if (customerRepository.existsByBusinessNumber(businessNumber)) {
            return ResponseDto.fail(ResponseCode.USER_ALREADY_EXISTS, ResponseMessage.USER_ALREADY_EXISTS);
        }

        Role role = roleRepository.findByName(UserRole.CUSTOMER)
                .orElseGet(() -> roleRepository.save(Role.builder().name(UserRole.CUSTOMER).build()));

        String encodePassword = bCryptPasswordEncoder.encode(password);

        User user = User.builder()
                .role(role)
                .username(username)
                .password(encodePassword)
                .email(email)
                .status(UserStatus.ACTIVE)
                .build();

        userRepository.save(user);

        if (profileImage != null && !profileImage.isEmpty()) {
            user.setProfileImage(uploadFileService.uploadProfile(profileImage, user.getId()));
        }

        Customer customer = Customer.builder()
                .user(user)
                .status(CustomerStatus.PENDING)
                .businessNumber(businessNumber)
                .name(dto.getName())
                .representativeName(dto.getRepresentativeName())
                .businessType(dto.getBusinessType())
                .businessItems(dto.getBusinessItems())
                .telephone(dto.getTelephone())
                .fax(dto.getFax())
                .businessZipCode(dto.getBusinessZipCode())
                .businessAddress(dto.getBusinessAddress())
                .businessAddressDetail(dto.getBusinessAddressDetail())
                .chargePosition(dto.getChargePosition())
                .chargeDepartment(dto.getChargeDepartment())
                .chargeName(dto.getChargeName())
                .chargePhone(dto.getChargePhone())
                .chargeEmail(dto.getChargeEmail())
                .parcelCount(0)
                .build();

        customerRepository.save(customer);

        String alertMessage = "[가입 신청] 가입 신청이 완료되었습니다. 관리자 승인 대기 중입니다.";
        alertService.sendToUser(customer.getUser().getId(), alertMessage);

        String alertMessageToAdmin = "[신규 가입 신청] " + customer.getName() + "님의 가입신청이 있습니다.";
        alertService.sendToManager(alertMessageToAdmin);

        data = CustomerSignUpResponseDto.builder()
                .id(customer.getId())
                .username(user.getUsername())
                .createdAt(DateUtils.format(customer.getCreatedAt()))
                .updatedAt(DateUtils.format(customer.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<LoginResponseDto> login(LoginRequestDto dto) {
        LoginResponseDto data = null;

        User user = userRepository.findByUsername(dto.getUsername())
                .orElse(null);

        if (user == null) {
            System.out.println(dto.getUsername());
            return ResponseDto.fail(ResponseCode.USER_NOT_FOUND, ResponseMessage.USER_NOT_FOUND);
        }

        if (!checkPassword(user, dto.getPassword())) {
            return ResponseDto.fail(ResponseCode.NOT_CORRECT_PASSWORD, ResponseMessage.NOT_CORRECT_PASSWORD);
        }

        if (user.isMustChangePassword()) {
            data = LoginResponseDto.builder()
                    .mustChangePassword(true)
                    .token(null)
                    .id(user.getId())
                    .role(user.getRole().getName())
                    .username(user.getUsername())
                    .name(user.getUsername())
                    .createdAt(DateUtils.format(user.getCreatedAt()))
                    .updatedAt(DateUtils.format(user.getUpdatedAt()))
                    .build();
            return ResponseDto.success(ResponseCode.PASSWORD_CHANGE_REQUIRED, ResponseMessage.PASSWORD_CHANGE_REQUIRED, data);
        }

        try {
            final String username = dto.getUsername() == null ? "" : dto.getUsername().trim();
            final String rawPassword = dto.getPassword();

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, rawPassword)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

            String token = jwtProvider.generateJwtToken(userPrincipal.getUsername(), user.getRole().getName());

            long exprTime = jwtProvider.getExpirationMs();

            data = LoginResponseDto.builder()
                    .token(token)
                    .exprTime(exprTime)
                    .id(user.getId())
                    .role(user.getRole().getName())
                    .username(user.getUsername())
                    .name(user.getUsername())
                    .createdAt(DateUtils.format(user.getCreatedAt()))
                    .updatedAt(DateUtils.format(user.getUpdatedAt()))
                    .build();

            return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
        } catch (DisabledException e) {
            return ResponseDto.fail(ResponseCode.FAILED, ResponseMessage.FAILED);
        } catch (BadCredentialsException e) {
            return ResponseDto.fail(ResponseCode.NOT_MATCH, ResponseMessage.NOT_MATCH);
        } catch (Exception e) {
            return ResponseDto.fail(ResponseCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseDto<UsernameCheckResponseDto> checkLoginIdDuplicate(String username) {
        String u = username == null ? "" : username.trim();
        boolean exists = userRepository.existsByUsername(u);
        UsernameCheckResponseDto data = UsernameCheckResponseDto.builder()
                .username(u)
                .exists(exists)
                .build();
        return ResponseDto.success(ResponseCode.SUCCESS, exists ? "이미 사용 중인 아이디입니다." : "사용 가능한 아이디입니다.", data);
    }

    @Override
    public ResponseDto<EmailCheckResponseDto> checkEmailDuplicate(String email) {
        String e = email == null ? "" : email.trim();
        boolean exists = userRepository.existsByEmail(e);
        EmailCheckResponseDto data = EmailCheckResponseDto.builder()
                .email(e)
                .exists(exists)
                .build();
        return ResponseDto.success(ResponseCode.SUCCESS, exists ? "이미 사용 중인 이메일입니다." : "사용 가능한 이메일입니다.", data);
    }

    @Override
    public ResponseDto<BusinessNumberCheckResponseDto> checkBusinessNumberDuplicate(String businessNumber) {
        String b = businessNumber == null ? "" : businessNumber.trim();
        boolean exists = customerRepository.existsByBusinessNumber(b);
        BusinessNumberCheckResponseDto data = BusinessNumberCheckResponseDto.builder()
                .businessNumber(b)
                .exists(exists)
                .build();
        return ResponseDto.success(ResponseCode.SUCCESS, exists ? "이미 사용 중인 사업자 번호입니다." : "사용 가능한 사업자 번호입니다.", data);
    }

    @Override
    public ResponseDto<CustomerLoginIdFindResponseDto> findCustomerLoginId(CustomerLoginIdFindRequestDto dto) {
        CustomerLoginIdFindResponseDto data = null;

        String email = dto.getEmail().trim();

        User user = userRepository.findByEmail(email)
                .orElse(null);

        if (user == null) {
            return ResponseDto.fail(ResponseCode.USER_NOT_FOUND, ResponseMessage.USER_NOT_FOUND);
        }

        Customer customer = customerRepository.findByUser(user)
                .orElse(null);

        if (customer == null) {
            return ResponseDto.fail(ResponseCode.USER_NOT_FOUND, ResponseMessage.USER_NOT_FOUND);
        }

        if (!customer.getBusinessNumber().equals(dto.getBusinessNumber())
                || !customer.getRepresentativeName().equals(dto.getRepresentativeName())
                || !user.getEmail().equals(dto.getEmail())
        ) {
            return ResponseDto.fail(ResponseCode.NOT_MATCH_INFORMATION, ResponseMessage.NOT_MATCH_INFORMATION);
        }

        data = CustomerLoginIdFindResponseDto.builder()
                .username(customer.getUser().getUsername())
                .createdAt(DateUtils.format(customer.getCreatedAt()))
                .updatedAt(DateUtils.format(customer.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<UserLoginIdFindResponseDto> findUserLoginId(UserLoginIdFindRequestDto dto) {
        UserLoginIdFindResponseDto data = null;

        String name = dto.getName().trim();
        String phone = dto.getPhoneNumber().replaceAll("\\D", "");

        Optional<Driver> driver = driverRepository.findByNameAndPhoneNumber(name, phone);
        Optional<Employee> employee = employeeRepository.findByNameAndPhoneNumber(name, phone);

        List<User> matchedUsers = new ArrayList<>();
        driver.map(Driver::getUser).ifPresent(matchedUsers::add);
        employee.map(Employee::getUser).ifPresent(matchedUsers::add);

        if (matchedUsers.isEmpty()) {
            return ResponseDto.fail(ResponseCode.USER_NOT_FOUND, ResponseMessage.USER_NOT_FOUND);
        }

        Set<String> usernames = matchedUsers.stream().map(User::getUsername).collect(Collectors.toSet());
        if (usernames.size() > 1) {
            return ResponseDto.fail(ResponseCode.USER_CONFLICT, ResponseMessage.USER_CONFLICT);
        }

        String username = usernames.iterator().next();

        User user = matchedUsers.get(0);

        data = UserLoginIdFindResponseDto.builder()
                .username(username)
                .createdAt(DateUtils.format(user.getCreatedAt()))
                .updatedAt(DateUtils.format(user.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<CustomerPasswordResetResponseDto> requestPasswordResetCustomer(CustomerPasswordResetRequestDto dto) {
        CustomerPasswordResetResponseDto data = null;

        User user = userRepository.findByUsername(dto.getUsername())
                .orElse(null);

        if (user == null) {
            return ResponseDto.fail(ResponseCode.USER_NOT_FOUND, ResponseMessage.USER_NOT_FOUND);
        }

        Customer customer = customerRepository.findByUser(user)
                .orElse(null);

        if (customer == null) {
            return ResponseDto.fail(ResponseCode.USER_NOT_FOUND, ResponseMessage.USER_NOT_FOUND);
        }

        if (!customer.getUser().getUsername().equals(dto.getUsername())
                || !customer.getBusinessNumber().equals(dto.getBusinessNumber())
                || !customer.getRepresentativeName().equals(dto.getRepresentativeName())
                || !customer.getUser().getEmail().equals(dto.getEmail())
        ) {
            return ResponseDto.fail(ResponseCode.NOT_MATCH_INFORMATION, ResponseMessage.NOT_MATCH_INFORMATION);
        }

        String token = jwtProvider.generateResetPasswordJwtToken(dto.getEmail());
        System.out.println("RESET TOKEN = " + token);

        try {
            mailService.sendResetPasswordEmail(
                    user.getEmail(),
                    user.getUsername(),
                    token
            );
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.fail(ResponseCode.MAIL_SEND_FAIL, ResponseMessage.MAIL_SEND_FAIL);
        }

        String alertMessage = "[비밀번호 재설정] 비밀번호 재설정 메일을 전송했습니다. 메일함을 확인해주세요.";
        alertService.sendToUser(customer.getUser().getId(), alertMessage);

        data = CustomerPasswordResetResponseDto.builder()
                .userId(customer.getUser().getId())
                .email(customer.getUser().getEmail())
                .createdAt(DateUtils.format(customer.getCreatedAt()))
                .updatedAt(DateUtils.format(customer.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<UserPasswordResetResponseDto> requestPasswordResetUser(UserPasswordResetRequestDto dto) {
        UserPasswordResetResponseDto data = null;

        String username = dto.getUsername().trim();
        String name = dto.getName().trim();
        String phoneNumber = dto.getPhoneNumber().replaceAll("\\D", "");
        String email = dto.getEmail().trim();

        User user = userRepository.findByUsername(username)
                .orElse(null);

        if (user == null) {
            return ResponseDto.fail(ResponseCode.USER_NOT_FOUND, ResponseMessage.USER_NOT_FOUND);
        }

        Optional<Driver> driverOpt = driverRepository.findByUser(user);
        Optional<Employee> employeeOpt = employeeRepository.findByUser(user);

        boolean valid = false;

        if (employeeOpt.isPresent()) {
            Employee employee = employeeOpt.get();
            valid = employee.getName().equals(name)
                    && employee.getPhoneNumber().equals(phoneNumber)
                    && user.getEmail().equals(email);
        }

        if (driverOpt.isPresent()) {
            Driver driver = driverOpt.get();
            valid = driver.getName().equals(name)
                    && driver.getPhoneNumber().equals(phoneNumber)
                    && user.getEmail().equals(email);
        }

        if (!valid) {
            return ResponseDto.fail(ResponseCode.NOT_MATCH_INFORMATION, ResponseMessage.NOT_MATCH_INFORMATION);
        }

        String token = jwtProvider.generateResetPasswordJwtToken(dto.getEmail());

        try {
            mailService.sendResetPasswordEmail(
                    user.getEmail(),
                    user.getUsername(),
                    token
            );
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.fail(ResponseCode.MAIL_SEND_FAIL, ResponseMessage.MAIL_SEND_FAIL);
        }

        String alertMessage = "[비밀번호 재설정] 비밀번호 재설정 메일을 전송했습니다. 메일함을 확인해주세요.";
        alertService.sendToUser(user.getId(), alertMessage);

        data = UserPasswordResetResponseDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .createdAt(DateUtils.format(user.getCreatedAt()))
                .updatedAt(DateUtils.format(user.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<PasswordResetSendEmailResponseDto> resetPassword(String token, PasswordResetRequestDto dto) {
        String email = jwtProvider.getEmailFromJwtToken(token);

        if (email == null) {
            return ResponseDto.fail(ResponseCode.INVALID_TOKEN, ResponseMessage.INVALID_TOKEN);
        }

        User user = userRepository.findByEmail(email)
                .orElse(null);

        if (user == null) {
            return ResponseDto.fail(ResponseCode.USER_NOT_FOUND, ResponseMessage.USER_NOT_FOUND);
        }

        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            return ResponseDto.fail(ResponseCode.NOT_MATCH_PASSWORD, ResponseMessage.NOT_MATCH_PASSWORD);
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);

        String alertMessage = "[비밀번호 변경] 비밀번호가 성공적으로 변경되었습니다.";
        alertService.sendToUser(user.getId(), alertMessage);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    @Override
    public ResponseDto<PasswordResetSendEmailResponseDto> verifyEmail(String token) {
        if (token == null) {
            return ResponseDto.fail(ResponseCode.MISSING_TOKEN, ResponseMessage.MISSING_TOKEN);
        }

        String email = jwtProvider.getEmailFromJwtToken(token);
        boolean isEmailVerified = checkEmail(email);

        if (!isEmailVerified) {
            return ResponseDto.fail(ResponseCode.MAIL_NOT_FOUND, ResponseMessage.MAIL_NOT_FOUND);
        }

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    @Override
    public boolean checkPassword(User user, String password) {
        return bCryptPasswordEncoder.matches(password, user.getPassword());
    }

    @Override
    public ResponseDto<FirstPasswordChangeResponseDto> firstChange(FirstPasswordChangeRequestDto dto) {
        FirstPasswordChangeResponseDto data = null;

        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(ResponseMessage.USER_NOT_FOUND));

        if (!user.isMustChangePassword()) {
            return ResponseDto.fail(ResponseCode.FAILED, "이미 비밀번호를 변경하였습니다.");
        }

        if (!passwordEncoder.matches(dto.getPrevPassword(), user.getPassword())) {
            return ResponseDto.fail(ResponseCode.NOT_MATCH_PASSWORD, ResponseMessage.NOT_MATCH_PASSWORD);
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        user.setMustChangePassword(false);
        userRepository.save(user);

        String alertMessage = "[초기 비밀번호 변경] 변경이 완료되었습니다. 이제 정상적으로 로그인할 수 있습니다.";
        alertService.sendToUser(user.getId(), alertMessage);

        data = FirstPasswordChangeResponseDto.builder()
                .username(user.getUsername())
                .createdAt(DateUtils.format(user.getCreatedAt()))
                .updatedAt(DateUtils.format(user.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public boolean checkEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }


    public class PasswordUtils {
        public static String generateTempPassword() {
            int length = 10;
            String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
            StringBuilder sb = new StringBuilder();
            SecureRandom random = new SecureRandom();
            for (int i = 0; i < length; i++) {
                sb.append(chars.charAt(random.nextInt(chars.length())));
            }
            return sb.toString();
        }
    }
}