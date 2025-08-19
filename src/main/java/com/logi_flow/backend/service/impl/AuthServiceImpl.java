package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.constants.ResponseCode;
import com.logi_flow.backend.common.constants.ResponseMessage;
import com.logi_flow.backend.common.enums.CustomerStatus;
import com.logi_flow.backend.common.enums.user.UserRole;
import com.logi_flow.backend.common.enums.user.UserStatus;
import com.logi_flow.backend.config.security.UserPrincipal;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.auth.request.*;
import com.logi_flow.backend.dto.auth.response.*;
import com.logi_flow.backend.entity.*;
import com.logi_flow.backend.provider.JwtProvider;
import com.logi_flow.backend.repository.*;
import com.logi_flow.backend.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional
    public ResponseDto<CustomerSignUpResponseDto> signup(CustomerSignUpRequestDto dto) {
        CustomerSignUpResponseDto data = null;

        String username = dto.getUsername().trim();
        String password = dto.getPassword();
        String confirmPassword = dto.getConfirmPassword();
        String businessNumber = dto.getBusinessNumber().replaceAll("[^0-9]", "");
        String email = dto.getEmail().trim().toLowerCase();

        if (!password.equals(confirmPassword)) {
            return ResponseDto.fail(ResponseCode.NOT_MATCH_PASSWORD, ResponseMessage.NOT_MATCH_PASSWORD);
        }

        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            return ResponseDto.fail(ResponseCode.USER_ALREADY_EXISTS, ResponseMessage.USER_ALREADY_EXISTS);
        }

        if (customerRepository.existsByEmail(email)) {
            return ResponseDto.fail(ResponseCode.USER_ALREADY_EXISTS, ResponseMessage.USER_ALREADY_EXISTS);
        }

        if (customerRepository.existsByBusinessNumber(businessNumber)) {
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
                .status(UserStatus.ACTIVE)
                .build();

        userRepository.save(user);

        Customer customer = Customer.builder()
                .user(user)
                .status(CustomerStatus.PENDING)
                .businessNumber(businessNumber)
                .name(dto.getName())
                .representativeName(dto.getRepresentativeName())
                .businessType(dto.getBusinessType())
                .businessItems(dto.getBusinessItems())
                .telephone(dto.getTelephone())
                .email(dto.getEmail())
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

        data = CustomerSignUpResponseDto.builder()
                .id(customer.getId())
                .username(user.getUsername())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<LoginResponseDto> login(LoginRequestDto dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElse(null);

        if (user == null) {
            return ResponseDto.fail(ResponseCode.USER_NOT_FOUND, ResponseMessage.USER_NOT_FOUND);
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

            LoginResponseDto data = LoginResponseDto.builder()
                    .token(token)
                    .exprTime(exprTime)
                    .id(user.getId())
                    .role(user.getRole().getName())
                    .username(user.getUsername())
                    .name(user.getUsername())
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
        if (userRepository.existsByUsername(username)) {
            return ResponseDto.fail(ResponseCode.ALREADY_EXISTS, ResponseMessage.ALREADY_EXISTS);
        }
        return ResponseDto.success(ResponseCode.SUCCESS, "사용 가능한 아이디입니다.");
    }

    @Override
    public ResponseDto<EmailCheckResponseDto> checkEmailDuplicate(String email) {
        if (customerRepository.existsByEmail(email)) {
            return ResponseDto.fail(ResponseCode.ALREADY_EXISTS, ResponseMessage.ALREADY_EXISTS);
        }
        return ResponseDto.success(ResponseCode.SUCCESS, "사용 가능한 이메일입니다.");
    }

    @Override
    public ResponseDto<BusinessNumberCheckResponseDto> checkBusinessNumberDuplicate(String businessNumber) {
        if (customerRepository.existsByBusinessNumber(businessNumber)) {
            return ResponseDto.fail(ResponseCode.ALREADY_EXISTS, ResponseMessage.ALREADY_EXISTS);
        }
        return ResponseDto.success(ResponseCode.SUCCESS, "사용 가능한 사업자 번호입니다.");
    }

    @Override
    public ResponseDto<LogoutResponseDto> logout(HttpServletResponse response) {
        return null;
    }

    @Override
    public ResponseDto<CustomerLoginIdFindResponseDto> findCustomerLoginId(CustomerLoginIdFindRequestDto dto) {
        Customer customer = customerRepository.findByEmail(dto.getEmail())
                .orElse(null);

        if (customer == null) {
            return ResponseDto.fail(ResponseCode.USER_NOT_FOUND, ResponseMessage.USER_NOT_FOUND);
        }

        if (!customer.getBusinessNumber().equals(dto.getBusinessNumber())
                || !customer.getRepresentativeName().equals(dto.getRepresentativeName())
                || !customer.getEmail().equals(dto.getEmail())
        ) {
            return ResponseDto.fail(ResponseCode.NOT_MATCH_INFORMATION, ResponseMessage.NOT_MATCH_INFORMATION);
        }

        CustomerLoginIdFindResponseDto data = new CustomerLoginIdFindResponseDto(customer.getUser().getUsername());

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<UserLoginIdFindResponseDto> findUserLoginId(UserLoginIdFindRequestDto dto) {
        String name = dto.getName().trim();
        String phone = dto.getPhone().replaceAll("\\D", "");

        Optional<Driver> driver = driverRepository.findByNameAndPhoneNumber(name, phone);
        Optional<Employee> employee = employeeRepository.findByNameAndPhone(name, phone);

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

        UserLoginIdFindResponseDto data = new UserLoginIdFindResponseDto(username);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
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