package com.logi_flow.backend.service;

import jakarta.mail.MessagingException;

public interface MailService {
    void sendMail(String email, String title, String content) throws MessagingException;
    void sendResetPasswordEmailAdmin(String email, String username, String representativeName, String tempPassword) throws MessagingException;
    void sendResetPasswordEmail(String email, String username, String token) throws MessagingException;
}
