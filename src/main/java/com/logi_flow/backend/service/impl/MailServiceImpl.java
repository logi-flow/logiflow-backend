package com.logi_flow.backend.service.impl;

import com.logi_flow.backend.service.MailService;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MailServiceImpl implements MailService {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Value("${app.frontend.url:http://localhost:5173}")
    private String frontendUrl;

    @Override
    public void sendMail(String email, String title, String content) throws MessagingException {
            MimeMessage message = javaMailSender.createMimeMessage();

            message.setFrom(sender);
            message.setRecipients(Message.RecipientType.TO, email);
            message.setSubject(title);
            message.setText(content, "utf-8", "html");

            javaMailSender.send(message);
    }

    @Override
    public void sendResetPasswordEmailAdmin(String email, String username, String representativeName, String tempPassword) throws MessagingException {
        String title = "[logi-flow] 임시 비밀번호 안내";
        String content = String.format(
                "안녕하세요. " +representativeName + " 님, \n\n" +
                        "요청하신 계정의 임시 비밀번호를 발급해드립니다. \n\n" +
                        "임시 비밀번호: " + tempPassword + "\n\n" +
                        "로그인 후 반드시 비밀번호를 변경해주세요. \n\n" +
                        "감사합니다."
        );
        sendMail(email, title, content);
    }

    @Override
    public void sendResetPasswordEmail(String email, String username, String token) throws MessagingException {
        String resetLink = frontendUrl + "/auth/password/reset?token=" + token;
        String title = "[logi-flow] 비밀번호 재설정 안내";
        String content = """
                <html>
                    <body style="font-family: 'Pretendard', sans-serif;">
                        <h3>안녕하세요, %s 님</h3>
                        <p>비밀번호 재설정을 위해 아래 버튼을 클릭해주세요.</p>
                        <p>
                            <a href="%s"
                                style="display:inline-block;padding:10px 16px;background-color:#1976d2;color:#fff;text-decoration:none;border-radius:6px;">
                                비밀번호 재설정하기
                            </a>
                        </p>
                        <p>이 링크는 30분 후 만료됩니다.</p>
                        <p style="color:#888;">감사합니다.<br>Logi-Flow 팀 드림</p>
                    </body>
                </html>
                """.formatted(username, resetLink);

//                String.format(
//                "안녕하세요. " + username + " 님, \n\n" +
//                        "비밀번호 재설정을 위해 아래 링크를 클릭해주세요. \n\n" +
//                        "링크: " + resetLink + "\n\n" +
//                        "이 링크는 30분 후 만료됩니다. \n\n" +
//                        "감사합니다."
//        );
        sendMail(email, title, content);
    }
}
