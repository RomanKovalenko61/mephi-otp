package ru.mephi.mephiotp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.from:${spring.mail.username}}")
    private String fromEmail;

    public void sendCode(String toEmail, String code) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(fromEmail);
            msg.setTo(toEmail);
            msg.setSubject("Your OTP code");
            msg.setText("Your verification code is: " + code);

            mailSender.send(msg);
            log.info("Email with OTP sent to {}", toEmail);
        } catch (MailException ex) {
            log.error("Failed to send email to {}", toEmail, ex);
        }
    }
}
