package com.logi_flow.backend.service;

import com.logi_flow.backend.common.constants.ResponseMessage;
import com.logi_flow.backend.entity.Alert;
import com.logi_flow.backend.entity.User;
import com.logi_flow.backend.repository.AlertRepository;
import com.logi_flow.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UserRepository userRepository;

    // 특정 유저에게 알림 전송하기
    public void sendToUser(Long userId, String message) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Alert alert = Alert.builder()
                .user(user)
                .message(message)
                .build();

        alertRepository.save(alert);

        // 저장 먼저하고 전송
        simpMessagingTemplate.convertAndSend("/queue/user-" + userId, message);
    }

    // 전체 유저에게 알림 전송하기
    // adminUser가 user_id로 들어가있으면 전체알림임
    public void sendToAll(String message) {
        User adminUser = userRepository.findByUsername("ADMIN").orElseThrow(() -> new EntityNotFoundException(ResponseMessage.USER_NOT_FOUND));

        Alert alert = Alert.builder()
                .user(adminUser)
                .message(message)
                .build();

        alertRepository.save(alert);

        simpMessagingTemplate.convertAndSend("/topic/alerts", message);
    }
}
