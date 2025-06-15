package ru.mephi.mephiotp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mephi.mephiotp.model.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final EmailNotificationService emailService;

    public void sendCodeToEmail(User user, String code) {
        if (user.getEmail() != null) {
            emailService.sendCode(user.getEmail(), code);
        }
    }

    public void saveCodeToFile(User user, String code) {
        try {
            String fileName = "user_" + user.getId() + "_otp.txt";
            Files.writeString(
                    Paths.get(fileName),
                    "User " + user.getUsername() + ", code: " + code + System.lineSeparator(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            log.error("IN saveCodeToFile - error: {}", e.getMessage());
        }
    }
}
