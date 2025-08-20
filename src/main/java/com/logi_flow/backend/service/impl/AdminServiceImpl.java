package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.common.constants.ResponseCode;
import com.logi_flow.backend.common.constants.ResponseMessage;
import com.logi_flow.backend.common.util.DateUtils;
import com.logi_flow.backend.dto.ResponseDto;
import com.logi_flow.backend.dto.admin.request.AdminResetPasswordRequestDto;
import com.logi_flow.backend.dto.admin.response.AdminResetPasswordResponseDto;
import com.logi_flow.backend.entity.Customer;
import com.logi_flow.backend.entity.User;
import com.logi_flow.backend.repository.CustomerRepository;
import com.logi_flow.backend.repository.UserRepository;
import com.logi_flow.backend.service.AdminService;
import com.logi_flow.backend.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MailService mailService;

    @Transactional
    @Override
    public ResponseDto<AdminResetPasswordResponseDto> adminPasswordResetCustomer(AdminResetPasswordRequestDto dto) {
        AdminResetPasswordResponseDto data = null;

        String username = dto.getUsername().trim();

        User user = userRepository.findByUsername(username)
                .orElse(null);

        if (user == null) {
            return ResponseDto.fail(ResponseCode.USER_NOT_FOUND, ResponseMessage.USER_NOT_FOUND);
        }

        Customer customer = customerRepository.findByUser(user)
                .orElse(null);

        if (customer == null) {
            return ResponseDto.fail(ResponseCode.USER_NOT_FOUND, ResponseMessage.USER_NOT_FOUND);
        }

        if (!customer.getUser().getUsername().equals(username)
        ) {
            return ResponseDto.fail(ResponseCode.NOT_MATCH_INFORMATION, ResponseMessage.NOT_MATCH_INFORMATION);
        }

        String tempPassword = AuthServiceImpl.PasswordUtils.generateTempPassword();
        String prevPassword = user.getPassword();

        try {
            user.setPassword(passwordEncoder.encode(tempPassword));
            userRepository.save(user);

            mailService.sendResetPasswordEmailAdmin(
                    user.getEmail(),
                    user.getUsername(),
                    customer.getRepresentativeName(),
                    tempPassword
            );
        } catch (Exception e) {
            user.setPassword(prevPassword);
            userRepository.save(user);
            return ResponseDto.fail(ResponseCode.MAIL_SEND_FAIL, ResponseMessage.MAIL_SEND_FAIL);
        }

        data = AdminResetPasswordResponseDto.builder()
                .userId(customer.getUser().getId())
                .email(user.getEmail())
                .createdAt(DateUtils.format(customer.getCreatedAt()))
                .updatedAt(DateUtils.format(customer.getUpdatedAt()))
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, data);
    }
}
